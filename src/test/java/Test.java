import org.wolflink.minecraft.wolfird.framework.ioc.Inject;
import org.wolflink.minecraft.wolfird.framework.ioc.Singleton;

public class Test {
    public static void main(String[] args) {

//        A a = IOC.getBean(A.class);

//        a.test();
        new Thread(()->IOC.getBean(B.class)).start();
        new Thread(()->IOC.getBean(B.class)).start();
        new Thread(()->IOC.getBean(B.class)).start();
        new Thread(()->IOC.getBean(B.class)).start();
        new Thread(()->IOC.getBean(B.class)).start();
    }
}
class A {
    @Inject
    B b;
    public A(){
        System.out.println("初始化A");
    }
    public void test(){
        System.out.println(b.getClass());
    }
}
class B {
    @Inject
    C c;
    public B(){
        System.out.println("初始化B");
    }
}
class C {
}