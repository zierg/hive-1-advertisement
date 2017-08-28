package homework.hive.task3;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.JavaStringObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

//  add jar hive-1-advertisement-1.0-SNAPSHOT-all.jar;
// create temporary function get_user_agent as 'homework.hive.task3.UserAgentParserUDF';
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAgentParserUDF extends GenericUDF {

    StringObjectInspector inspector;

    @Override
    public String[] evaluate(DeferredObject[] arguments) throws HiveException {
        String userAgentString = inspector.getPrimitiveJavaObject(arguments[0].get());
        if (userAgentString == null) {
            return null;
        }
        UserAgent agent = UserAgent.parseUserAgentString(userAgentString);
        String device = agent.getOperatingSystem().getDeviceType().getName();
        String browser = agent.getBrowser().getGroup().getName();
        String os = agent.getOperatingSystem().getGroup().getName();
        return new String[] {device, browser, os};
    }

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        check(() -> arguments.length == 1, () -> "User Agent Parser takes only one argument");
        check(() -> arguments[0] instanceof StringObjectInspector, () -> "User Agent Parser allows using only string values");
        inspector = (StringObjectInspector) arguments[0];
        return createOutputObjectInspector();
    }

    @Override
    public String getDisplayString(String[] children) {
        return "User Agent Parser";
    }

    private void check(Supplier<Boolean> condition, Supplier<String> errorMessage) throws UDFArgumentException {
        if (!condition.get()) {
            throw new UDFArgumentException(errorMessage.get());
        }
    }

    private StructObjectInspector createOutputObjectInspector() {
        List<String> structFields = Arrays.asList("device", "browser", "os");
        JavaStringObjectInspector inspector = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
        List<ObjectInspector> structInspectors = Arrays.asList(inspector, inspector, inspector, inspector);
        return ObjectInspectorFactory.getStandardStructObjectInspector(structFields, structInspectors);
    }
}
