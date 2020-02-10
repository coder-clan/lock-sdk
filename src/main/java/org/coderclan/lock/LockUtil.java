package org.coderclan.lock;

import com.google.gson.Gson;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by aray.chou.cn(at)gmail(dot)com on 3/15/2018.
 */
@Component
public class LockUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LockUtil.applicationContext = applicationContext;
    }

    static <T> T getBean(Class<T> interfaceType) {
        return applicationContext.getBean(interfaceType);
    }

    static LockService getLockService() {
        return applicationContext.getBean(LockService.class);
    }

    private static Gson gson = new Gson();

    static Gson gson() {
        return gson;
    }
}
