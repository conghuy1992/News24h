package news24.conghuy.com.news24h.common.interfaces;

import java.util.List;

import news24.conghuy.com.news24h.model.XmlDto;

/**
 * Created by maidinh on 7/25/2017.
 */

public interface XmlDataCallBack {
    void onSuccess(List<XmlDto> xmlDtoList);
    void onFail();
}
