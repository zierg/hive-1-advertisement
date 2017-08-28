package test.homework.hive.task3;

import homework.hive.task3.UserAgentParserUDF;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.IntObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.apache.hadoop.hive.ql.udf.generic.GenericUDF.DeferredObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAgentParserUDFTest {

    @Test
    public void givenZeroArgumentsWhenInitializeThenThrowException() throws UDFArgumentException {
        exception.expect(UDFArgumentException.class);
        exception.expectMessage("User Agent Parser takes only one argument");
        tested.initialize(new ObjectInspector[] {});
    }

    @Test
    public void givenTwoArgumentsWhenInitializeThenThrowException() throws UDFArgumentException {
        ObjectInspector inspector1 = mock(ObjectInspector.class);
        ObjectInspector inspector2 = mock(ObjectInspector.class);

        exception.expect(UDFArgumentException.class);
        exception.expectMessage("User Agent Parser takes only one argument");
        tested.initialize(new ObjectInspector[] {inspector1, inspector2});
    }

    @Test
    public void givenOneNonStringArgumentWhenInitializeThenThrowException() throws UDFArgumentException {
        IntObjectInspector inspector = mock(IntObjectInspector.class);

        exception.expect(UDFArgumentException.class);
        exception.expectMessage("User Agent Parser allows using only string values");
        tested.initialize(new ObjectInspector[] {inspector});
    }

    @Test
    public void givenOneStringArgumentWhenInitializeThenReturnStructObjectInspector() throws UDFArgumentException {
        ObjectInspector result = tested.initialize(new ObjectInspector[]{correctStringObjectInspector});

        assertThat(result).isInstanceOf(StructObjectInspector.class);
        val inspector = (StructObjectInspector) result;
        List<? extends StructField> structFields = inspector.getAllStructFieldRefs();
        verifyField(structFields.get(0), "device");
        verifyField(structFields.get(1), "browser");
        verifyField(structFields.get(2), "os");
    }

    @Test
    public void givenNullWhenEvaluateThenReturnNull() throws HiveException {
        DeferredObject object = mock(DeferredObject.class);
        when(correctStringObjectInspector.getPrimitiveJavaObject(object)).thenReturn(null);
        tested.initialize(new ObjectInspector[]{ correctStringObjectInspector});
        Object result = tested.evaluate(new DeferredObject[]{object});
        assertThat(result).isNull();
    }

    @Test
    public void givenUserAgentStringWhenEvaluateThenReturnStructFields() throws HiveException {
        DeferredObject object = mock(DeferredObject.class);
        String agent = "Mozilla/5.0 (compatible; MSIE 9.0;\\Windows NT 6.1; WOW64; Trident/5.0)";
        when(correctStringObjectInspector.getPrimitiveJavaObject(object)).thenReturn(agent);
        tested.initialize(new ObjectInspector[]{ correctStringObjectInspector});
        String[] result = tested.evaluate(new DeferredObject[]{object});
        assertThat(result).containsExactly("Computer", "Internet Explorer", "Windows");
    }

    private void verifyField(StructField structField, String fieldName) {
        assertThat(structField)
                .returns(fieldName, StructField::getFieldName)
                .returns(PrimitiveObjectInspectorFactory.writableStringObjectInspector, StructField::getFieldObjectInspector);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    StringObjectInspector correctStringObjectInspector;

    final UserAgentParserUDF tested = new UserAgentParserUDF();
}