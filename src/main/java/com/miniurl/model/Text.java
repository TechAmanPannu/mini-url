package com.miniurl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Text  implements Serializable {
    private static final long serialVersionUID = -4801264900920801628L;

    private String value;
}
