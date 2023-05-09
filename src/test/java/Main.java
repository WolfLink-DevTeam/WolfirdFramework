import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.wolflink.minecraft.wolfird.framework.WolfirdFramework;

public class Main {
    private ServerMock server;
    private WolfirdFramework plugin;

    public static Main INSTANCE = new Main();

    public static void main(String[] args) {
        INSTANCE.server = MockBukkit.mock();
        INSTANCE.plugin = MockBukkit.load(WolfirdFramework.class);
        MockBukkit.unmock();
    }
}
