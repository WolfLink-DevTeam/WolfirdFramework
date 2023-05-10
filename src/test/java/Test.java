import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.wolflink.minecraft.wolfird.framework.Framework;

public class Test {
    private ServerMock SERVER;
    private Framework PLUGIN;

    public static Test INSTANCE = new Test();

    public static void main(String[] args) {
        INSTANCE.SERVER = MockBukkit.mock();
        INSTANCE.PLUGIN = MockBukkit.load(Framework.class);
//        INSTANCE.SPRING = SpringApp.INSTANCE;
//
//        AddonContainer addonContainer = (AddonContainer) INSTANCE.SPRING.getBean("addonContainer");


//        MockBukkit.unmock();
    }
}
