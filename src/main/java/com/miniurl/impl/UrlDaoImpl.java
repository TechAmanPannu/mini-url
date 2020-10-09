package com.miniurl.impl;

import com.google.common.collect.Lists;
import com.miniurl.dao.UrlDao;
import com.miniurl.dao.UserDao;
import com.miniurl.entity.Url;;
import com.miniurl.entity.User;
import com.miniurl.enums.UrlAccessType;
import com.miniurl.exception.EntityException;
import com.miniurl.exception.ForbiddenException;
import com.miniurl.exception.enums.EntityErrorCode;
import com.miniurl.model.request.UrlCreateRequest;
import com.miniurl.model.request.UrlUpdateRequest;
import com.miniurl.model.response.CollectionResponse;
import com.miniurl.utils.*;
import com.miniurl.validator.UrlValidator;
import com.miniurl.zookeeper.keycounter.KeyCounter;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;


@Service
@Slf4j
public class UrlDaoImpl extends UrlValidator implements UrlDao {

    @Autowired
    private KeyCounter keyCounter;

    @Autowired
    private Datastore datastore;

    @Autowired
    private UserDao userDao;


    @Override
    public Url save(String id, Url url) {

        Preconditions.checkArgument(url == null, "Invalid url to save");
        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid url id to save");

        url.setId(id);
        return datastore.save(url) != null ? url : null ;
    }

    public Url get(String id) {
        return datastore.createQuery(Url.class)
                .field("id").equal(id).first();
    }

    @Override
    public Url get(String domain, String linkId) {
        return get(generateId(domain, linkId));
    }

    @Override
    public Url create(UrlCreateRequest urlCreateRequest) throws EntityException {


        super.validate(urlCreateRequest);

        if (urlCreateRequest.getAccessType() == null)
            urlCreateRequest.setAccessType(UrlAccessType.PUBLIC);

        User user = userDao.get(urlCreateRequest.getCreatedBy());

        Url url = new Url(urlCreateRequest.getUrl());
        url.setAccessType(urlCreateRequest.getAccessType());
        url.setUsers(urlCreateRequest.getUserIds());
        url.setCreatedBy(user.getId());
        url.setAcctId(user.getAcctId());
        url.setDomain(urlCreateRequest.getDomain());
        url.setLinkId(ObjUtil.isBlank(urlCreateRequest.getCustomLinkId()) ? EncodeUtil.Base62.encode(keyCounter.getCountAndIncr()) : urlCreateRequest.getCustomLinkId());
        url.setId(generateId(url.getDomain(), url.getLinkId()));

        url = save(url.getId(), url);

        if (url == null)
            throw new EntityException(EntityErrorCode.CREATE_FAILED, "Failed to create url");

        if (ObjUtil.isBlank(url.getCreatedBy()))
            return url;

        return url;
    }


    @Override
    public List<Url> createBulk(UrlCreateRequest urlCreateRequest) throws EntityException {

        Preconditions.checkArgument(urlCreateRequest == null, "Invalid url request body to create bulk mini urls");
        Preconditions.checkArgument(ObjUtil.isNullOrEmpty(urlCreateRequest.getUrls()), "No urls found to create bulk mini urls");
        Preconditions.checkArgument(urlCreateRequest.getUrls().size() > 20, "Urls cannot be created more than 20 limit, use import urls");

        List<List<String>> partitionedRawUrls = Lists.partition(urlCreateRequest.getUrls(), (urlCreateRequest.getUrls().size() / ThreadUtil.NUMBER_OF_THREADS_PER_REQUEST) + 1);
        ExecutorService executorService = ThreadUtil.getRequestExecutorService();

        List<Url> urls = new ArrayList<>();
        try {

            List<CompletableFuture> futures = new ArrayList<>();
            for (List<String> partitionedUrl : partitionedRawUrls)
                futures.add(createBulkAsync(new UrlCreateRequest(null, urlCreateRequest.getCreatedBy(), partitionedUrl), executorService));
            urls = getUrlsFromFutureResults(ThreadUtil.awaitFutureAll(futures));

        } catch (Exception e) {
            log.error("Exception while creating bulk urls");
            e.printStackTrace();
        } finally {
            log.info("shutting down Executor Service");
            executorService.shutdown();
        }
        return urls;
    }

    @Override
    public CompletableFuture<List<Url>> createBulkAsync(UrlCreateRequest urlCreateRequest, ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> createBulk(urlCreateRequest.getUrls(), urlCreateRequest.getCreatedBy()), executorService);
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public boolean delete(Set<String> ids) {
        return false;
    }


    @Override
    public List<Url> getByIds(Set<String> ids) {
        return null;
    }

    @Override
    public boolean deleteUserUrls(String appUrl, Set<String> urlIds) {
        return false;
    }

    @Override
    public Url updateUrlAccess(String urlId, String userId, UrlUpdateRequest urlUpdateRequest) throws ForbiddenException {
        return null;
    }

    @Override
    public CollectionResponse<Url> getUserUrls(String userId, UrlAccessType accessType,
                                          Long since, Long startTime, Long endTime, Integer limit, Integer offSet) {

        Preconditions.checkArgument(ObjUtil.isBlank(userId), "Invalid userId to pull urls");

        offSet = offSet == null ? 0 : offSet;
        since = since == null ? 0 : since;
        startTime = startTime == null ? 0 : startTime;
        endTime = endTime == null ? 0 : endTime;

        User user = userDao.get(userId);
        Preconditions.checkArgument(user == null, "Invalid user to get urls");

        if (limit == null || limit <= 0 || limit > 100)
            limit = 100;


        Query<Url> query = datastore.createQuery(Url.class);
        query = query.field("createdBy").equal(userId);
        FindOptions findOptions = new FindOptions().batchSize(limit);


        if(since > 0){
          query =  query.field("modifiedAt").greaterThan(since);
            query.find(findOptions.skip(offSet)).toList();
          return new CollectionResponse<>( query.find().toList(), limit);
        }

        if(endTime <= 0)
            endTime = System.currentTimeMillis();



        return null;

    }

    public String generateId(String domain, String linkId) {

        StringBuilder sb = new StringBuilder("Url:");

        if(!ObjUtil.isBlank(domain))
            sb.append(domain);

        if(!ObjUtil.isBlank(linkId))
            sb.append(linkId);

        return HashUtil.sha256(sb.toString());
    }

    private List<Url> getUrlsFromFutureResults(List<Object> results) {

        List<Url> urls = new ArrayList<>();

        for (Object object : results)
            urls.addAll(Objects.requireNonNull(Url.asList(ObjUtil.getJson(object))));

        return urls;
   }

    private List<Url> createBulk(List<String> rawUrls, String createdBy) {

        List<Url> urls = new ArrayList<>();

        for (String rawUrl : rawUrls) {
            try {
                urls.add(create(new UrlCreateRequest(rawUrl, createdBy, null)));
            } catch (EntityException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

//    @Override
//    public Url create(UrlCreateRequest urlCreateRequest) throws EntityException {
//
//        log.info("time of request :" + System.currentTimeMillis());
//
//        Preconditions.checkArgument(urlCreateRequest == null, "Invalid url to save");
//        Preconditions.checkArgument(ObjUtil.isBlank(urlCreateRequest.getUrl()), "Invalid url string to create url");
//        Preconditions.checkArgument(urlCreateRequest.getAccessType() == AccessType.PRIVATE && ObjUtil.isBlank(urlCreateRequest.getCreatedBy()), "private url should contains created_by");
//
//        if (urlCreateRequest.getAccessType() == null)
//            urlCreateRequest.setAccessType(AccessType.PUBLIC);
//
//        String createdBy = ObjUtil.isBlank(urlCreateRequest.getCreatedBy()) ? AppConstants.APP_USER : urlCreateRequest.getCreatedBy();
//
//        Url url = new Url(urlCreateRequest.getUrl());
//        url.setAccessType(urlCreateRequest.getAccessType().getValue());
//        url.setUsers(urlCreateRequest.getUserIds());
//        url.setCreatedBy(createdBy);
//        url.setId(EncodeUtil.Base62.encode(keyCounter.getCountAndIncr()));
//
//        url = save(url.getId(), url);
//
//        if (url == null)
//            throw new EntityException(EntityErrorCode.CREATE_FAILED, "Failed to create url");
//
//        if (ObjUtil.isBlank(url.getCreatedBy()))
//            return url;
//
//        urlProducer.createUrlIndexes(url);
//        return url;
//    }
//
//
//    @Override
//    public List<Url> createBulk(UrlCreateRequest urlCreateRequest) {
//
//        Preconditions.checkArgument(urlCreateRequest == null, "Invalid url request body to create bulk mini urls");
//        Preconditions.checkArgument(ObjUtil.isNullOrEmpty(urlCreateRequest.getUrls()), "No urls found to create bulk mini urls");
//        Preconditions.checkArgument(urlCreateRequest.getUrls().size() > 20, "Urls cannot be created more than 20 limit, use import urls");
//
//        List<List<String>> partitionedRawUrls = Lists.partition(urlCreateRequest.getUrls(), (urlCreateRequest.getUrls().size() / ThreadUtil.NUMBER_OF_THREADS_PER_REQUEST) + 1);
//        ExecutorService executorService = ThreadUtil.getRequestExecutorService();
//
//        List<Url> urls = new ArrayList<>();
//        try {
//
//            List<CompletableFuture> futures = new ArrayList<>();
//            for (List<String> partitionedUrl : partitionedRawUrls)
//                futures.add(createBulkAsync(new UrlCreateRequest(null, urlCreateRequest.getCreatedBy(), partitionedUrl), executorService));
//            urls = getUrlsFromFutureResults(ThreadUtil.awaitFutureAll(futures));
//
//        } catch (Exception e) {
//            log.error("Exception while creating bulk urls");
//            e.printStackTrace();
//        } finally {
//            log.info("shutting down Executor Service");
//            executorService.shutdown();
//        }
//        return urls;
//    }
//
//
//
//    @Override
//    public CompletableFuture<List<Url>> createBulkAsync(UrlCreateRequest urlCreateRequest, ExecutorService executorService) {
//        return CompletableFuture.supplyAsync(() -> createBulk(urlCreateRequest.getUrls(), urlCreateRequest.getCreatedBy()), executorService);
//    }
//
//
//    /// todo need to something to expire the the properties and need to test
//    @CachePut(value = "url", key = "#id")
//    @Override
//    public Url save(String id, Url url) {
//
//        Preconditions.checkArgument(url == null, "Invalid url to save");
//        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid url id to save");
//
//        url.setId(id);
//        long time = System.currentTimeMillis();
//        if (url.getCreatedAt() <= 0)
//            url.setCreatedAt(time);
//        url.setModifiedAt(time);
//
//        try {
//            return urlRepository.save(url);
//        } catch (Exception e) {
//            log.error("Exception while saving url :", e.getMessage(), e);
//            return null;
//        }
//    }
//
//
//    @Cacheable(value = "url", key = "#id")
//    @Override
//    public Url get(String id) {
//        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid urlId to fetch url");
//        Optional<Url> optional = Optional.empty();
//        try {
//            optional = urlRepository.findById(id);
//        } catch (Exception e) {
//            log.error("Exception while fetching url :", e.getMessage(), e);
//        }
//        return optional.orElse(null);
//    }
//
//    @CacheEvict(value = "url", key = "#id")
//    @Override
//    public boolean delete(String id) {
//
//        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid urlId to delete url");
//        try {
//            urlRepository.deleteById(id);
//        } catch (Exception e) {
//            log.error("Exception while deleting url by Id:", e.getMessage(), e);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean delete(Set<String> ids) {
//        Preconditions.checkArgument(ObjUtil.isNullOrEmpty(ids), "Invalid urlIds to delete");
//        urlRepository.deleteAllByIds(ids);
//        return true;
//    }
//
//    @Override
//    public boolean delete(UrlUserAccessType urlUserAccessType) {
//        urlUserAccessTypeRepository.delete(urlUserAccessType);
//        return true;
//    }
//
//    @Override
//    public List<Url> fetchQuery(String createdBy, AccessType accessType, long modifiedAt) {
//
//        Preconditions.checkArgument(ObjUtil.isBlank(createdBy), "Invalid userId to get urls");
//
//        if (createdBy.equals(AppConstants.APP_USER))
//            return new ArrayList<>();
//
//        if (modifiedAt <= 0)
//            modifiedAt = System.currentTimeMillis();
//
//        List<UrlUserAccessType> urlUserAccessTypes = query(createdBy, accessType, modifiedAt);
//        if (ObjUtil.isNullOrEmpty(urlUserAccessTypes))
//            return new ArrayList<>();
//
//        Set<String> urlIds = urlUserAccessTypes.stream().map(UrlUserAccessType::getUrlId).collect(Collectors.toSet());
//        return getByIds(urlIds);
//    }
//
//    @Override
//    public List<Url> getByIds(Set<String> ids) {
//        return getAllByIds(ids);
//    }
//
//    @Override
//    public UrlUserAccessType save(UrlUserAccessType urlUserAccessType) {
//
//        Preconditions.checkArgument(urlUserAccessType == null, "Invalid urlCreatedInDescByUser to save");
//        Preconditions.checkArgument(ObjUtil.isBlank(urlUserAccessType.getUrlId()), "Invalid urlId to save urlCreatedInDescByUser");
//        Preconditions.checkArgument(ObjUtil.isBlank(urlUserAccessType.getCreatedBy()), "Invalid createdBy to save urlCreatedInDescByUser");
//        Preconditions.checkArgument(urlUserAccessType.getModifiedAt() <= 0, "Invalid modifiedAt to save urlCreatedInDescByUser");
//
//        try {
//            return urlUserAccessTypeRepository.save(urlUserAccessType);
//        } catch (Exception e) {
//            log.error("Exception while saving urlCreatedInDescByUser : ", e.getMessage(), e);
//            return null;
//        }
//    }
//
//    @Override
//    public boolean deleteUserUrls(String createdBy, Set<String> urlIds) {
//
//        Preconditions.checkArgument(ObjUtil.isNullOrEmpty(urlIds), "Invalid urlIds to delete");
//        Preconditions.checkArgument(ObjUtil.isBlank(createdBy), "Invalid createdBy to delete urls");
//        Preconditions.checkArgument(urlIds.size() > 20, "urlIds limit exceeded than 20");
//
//        List<Url> urls = getAllByIds(urlIds);
//        Preconditions.checkArgument(ObjUtil.isNullOrEmpty(urls), "No url found in db with urlIds to delete");
//
//        Iterator iterator = urls.iterator();
//
//        while (iterator.hasNext()) {
//
//            Url url = (Url) iterator.next();
//
//            if (url == null || !url.getCreatedBy().equals(createdBy))
//                iterator.remove();
//        }
//
//        urlRepository.deleteAll(urls);
//        return true;
//    }
//
//    @Override
//    public Url updateUrlAccess(String urlId, String userId, UrlUpdateRequest urlUpdateRequest) throws ForbiddenException {
//
//        Preconditions.checkArgument(urlUpdateRequest == null, "Invalid url update request");
//        Preconditions.checkArgument(ObjUtil.isBlank(urlId), "Invalid urlId to update access");
//        Preconditions.checkArgument(ObjUtil.isBlank(userId), "Invalid userId to update access");
//
//        Url oldUrl = get(urlId);
//        Preconditions.checkArgument(oldUrl == null, "Invalid url to update access");
//
//        AccessType oldAccessType = AccessType.fromValue(oldUrl.getAccessType());
//
//        Preconditions.checkArgument(oldAccessType == AccessType.PUBLIC && urlUpdateRequest.getAccessType() == AccessType.PRIVATE, "url is public, you can't make it private");
//        Preconditions.checkArgument(oldAccessType == AccessType.PRIVATE && urlUpdateRequest.getAccessType() == AccessType.PRIVATE, "url is already private" );
//        Preconditions.checkArgument(oldAccessType == AccessType.PUBLIC && urlUpdateRequest.getAccessType() == AccessType.PUBLIC, "url is already public");
//
//        if (!oldUrl.getCreatedBy().equals(userId))
//            throw new ForbiddenException("you are not authorized to update access");
//
//        Url newUrl = oldUrl.clone();
//
//        newUrl.setUsers(urlUpdateRequest.getUserIds());
//        newUrl.setAccessType(urlUpdateRequest.getAccessType().getValue());
//
//        newUrl = save(urlId, newUrl);
//
//        urlProducer.updateUrlIndexes(oldUrl, newUrl);
//        return newUrl;
//    }
//
//    @Override
//    public Url updateUrlIndexes(Url oldUrl, Url newUrl) {
//
//        Preconditions.checkArgument(oldUrl == null, "Invalid old url to update indexes");
//        Preconditions.checkArgument(newUrl == null, "Invalid new url to update indexes");
//
//        List<UrlUserAccessType> fetchedUrlUserAccessType = get(oldUrl.getCreatedBy(), AccessType.fromValue(oldUrl.getAccessType()), oldUrl.getModifiedAt());
//
//        if(!ObjUtil.isNullOrEmpty(fetchedUrlUserAccessType)){
//            for(UrlUserAccessType urlUserAccessType : fetchedUrlUserAccessType){
//                if(urlUserAccessType == null)
//                    continue;
//
//                if(urlUserAccessType.getUrlId().equals(oldUrl.getId())){
//                    delete(urlUserAccessType);
//                    break;
//                }
//            }
//        }
//
//        save(new UrlUserAccessType(newUrl.getCreatedBy(), newUrl.getModifiedAt(), newUrl.getId(), newUrl.getAccessType()));
//        return newUrl;
//    }
//
//    @Override
//    public Url createUrlIndexes(Url url) {
//
//        Preconditions.checkArgument(url == null, "Invalid url to create indexes");
//
//        save(new UrlUserAccessType(url.getCreatedBy(), url.getModifiedAt(), url.getId(), url.getAccessType()));
//
//        return url;
//    }
//
//    @Override
//    public List<UrlUserAccessType> get(String createdBy, AccessType accessType, long modifiedAt) {
//      return urlUserAccessTypeRepository.findByCreatedByAndAccessTypeAndModifiedAt(createdBy, accessType.getValue(), modifiedAt);
//
//    }
//
//
//    private boolean isUserUrl(Url url, String userId) {
//
//        Preconditions.checkArgument(url == null, "Invalid url to check user url");
//
//        AccessType accessType = AccessType.fromValue(url.getAccessType());
//        Preconditions.checkArgument(accessType == null, "Invalid access type to check user url");
//
//
//        if (accessType == AccessType.PUBLIC)
//            return true;
//
//        if (accessType == AccessType.PRIVATE) {
//            return url.getCreatedBy().equals(userId) || (url.getUsers() != null && url.getUsers().contains(userId));
//        }
//
//        return false;
//    }
//
//
//    private List<Url> getAllByIds(Set<String> ids) {
//        try {
//            return urlRepository.findAllById(ids);
//        } catch (Exception e) {
//            log.error("Exception while fetching all urls by ids :", e.getMessage(), e);
//            return new ArrayList<>();
//        }
//    }
//
//    private List<UrlUserAccessType> query(String createdBy, AccessType accessType, long modifiedAt) {
//        try {
//            return urlUserAccessTypeRepository.findByCreatedByAndAccessTypeAndModifiedAtLessThan(createdBy, accessType.getValue(), modifiedAt);
//        } catch (Exception e) {
//            log.error("Exception while fetching urlCreatedInDescByUser with userId and createdAt", e.getMessage(), e);
//            return new ArrayList<>();
//        }
//    }
//
//    private List<Url> createBulk(List<String> rawUrls, String createdBy) {
//
//        List<Url> urls = new ArrayList<>();
//
//        for (String rawUrl : rawUrls) {
//            try {
//                urls.add(create(new UrlCreateRequest(rawUrl, createdBy, null)));
//            } catch (EntityException e) {
//                e.printStackTrace();
//            }
//        }
//        return urls;
//    }

}
