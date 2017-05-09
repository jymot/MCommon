package im.wangchao.mcommon.reflect;

import java.lang.annotation.Annotation;

/**
 * <p>Description  : ReflectAnnotions.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 17/5/9.</p>
 * <p>Time         : 下午8:43.</p>
 */
public class ReflectAnnotation {
    private Annotation mAnnotation;

    private ReflectAnnotation(Annotation annotation){
        this.mAnnotation = annotation;
    }

    public static ReflectAnnotation create(Annotation annotation){
        return new ReflectAnnotation(annotation);
    }


}
