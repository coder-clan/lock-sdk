package org.coderclan.lock;

/**
 * Created by aray.chou.cn(at)gmail(dot)com on 3/15/2018.
 */
public class IdempotentElRoot {
    Object[] arg;

    public IdempotentElRoot(Object[] arg) {
        this.arg = arg;
    }

    public Object[] getArg() {
        return arg;
    }
}
