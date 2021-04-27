package com.zywl;

import com.zywl.opendubbbo.Dt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2021年04月26日 14:31:00
 */
@Component
public class Test {

    @Autowired
    private Dt dt;

    public void say(){
        dt.bb();
    }
}
