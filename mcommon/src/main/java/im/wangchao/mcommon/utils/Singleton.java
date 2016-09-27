package im.wangchao.mcommon.utils;

/**
 * <p>Description  : Singleton.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 16/8/24.</p>
 * <p>Time         : 下午5:45.</p>
 */
public abstract class Singleton<T> {
    private T instance;

    protected abstract T create();

    public T get(){
        if (instance == null){
            synchronized (this){
                if (instance == null){
                    instance = create();
                }
            }
        }

        return instance;
    }
}
