package net.redborder.samza.processors;

import junit.framework.TestCase;
import net.redborder.samza.store.StoreManager;
import net.redborder.samza.util.MockMessageCollector;
import net.redborder.samza.util.MockTaskContext;
import org.apache.samza.config.Config;
import org.apache.samza.config.ConfigException;
import org.apache.samza.task.TaskContext;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.redborder.samza.util.constants.Dimension.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProcessorTest extends TestCase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    static TaskContext taskContext;

    @BeforeClass
    public static void initTest() {
        taskContext = new MockTaskContext();
    }

    @Test
    public void getProcessorInstantiatesTheCorrectProcessor() {
        Config config = mock(Config.class);
        when(config.getList("redborder.processors.rb_flow")).thenReturn(Arrays.asList("net.redborder.samza.processors.FlowProcessor"));
        List<Processor> p = Processor.getProcessors("rb_flow", config, taskContext, null, null);
        assertEquals("flow", p.get(0).getName());
    }

    @Test
    public void getProcessorReturnsDummyWhenClassNotFound() {
        Config config = mock(Config.class);
        when(config.getList("redborder.processors.rb_nmsp")).thenReturn(Arrays.asList("net.redborder.samza.processors.NotFoundProcessor"));
        List<Processor> p = Processor.getProcessors("rb_nmsp", config, taskContext, null, null);
        assertEquals("dummy", p.get(0).getName());
    }

    @Test
    public void streamWithoutProcessorThrowsConfigException() {
        Config config = mock(Config.class);
        when(config.getList("redborder.processors.rb_nmsp")).thenThrow(new ConfigException("Not found"));
        exception.expect(ConfigException.class);
        List<Processor> p = Processor.getProcessors("rb_nmsp", config, taskContext, null, null);
    }

    @Test
    public void getProcessorWithoutEnrichmentsWorks() {
        Config config = mock(Config.class);
        when(config.get("redborder.enrichments.streams.rb_flow")).thenThrow(new ConfigException("Not found"));
        when(config.getList("redborder.processors.rb_flow")).thenReturn(Arrays.asList("net.redborder.samza.processors.FlowProcessor"));

        TaskContext context = mock(TaskContext.class);
        StoreManager storeManager = new StoreManager(config, context);
        List<Processor> processors = Processor.getProcessors("rb_flow", config, taskContext, storeManager, null);


        for(Processor processor : processors) {
            Map<String, Object> message = new HashMap<>();
            message.put(CLIENT_MAC, "AA:AA:AA:AA:AA:AA");
            message.put(BYTES, 43L);
            message.put(PKTS, 3L);
            message.put(TIMESTAMP, Long.valueOf(1429088471L));

            MockMessageCollector collector = new MockMessageCollector();
            processor.process(message, collector);

            Map<String, Object> result = collector.getResult().get(0);
            message.put(DURATION, 0L);
            assertEquals(message, result);
        }
    }
}


