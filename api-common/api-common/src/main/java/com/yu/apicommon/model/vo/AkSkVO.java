package com.yu.apicommon.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * ak、sk视图
 */
@Data
public class AkSkVO implements Serializable {

    private static final long serialVersionUID = -73582985163167047L;

    private String accessKey;

    private String secretKey;

}
