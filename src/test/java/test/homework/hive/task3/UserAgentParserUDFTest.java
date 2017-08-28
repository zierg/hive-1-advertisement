package test.homework.hive.task3;

import homework.hive.task3.UserAgentParserUDF;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
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
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
        StringObjectInspector inputInspector = mock(StringObjectInspector.class);
        ObjectInspector result = tested.initialize(new ObjectInspector[]{inputInspector});

        assertThat(result).isInstanceOf(StructObjectInspector.class);
        val inspector = (StructObjectInspector) result;
        List<? extends StructField> structFields = inspector.getAllStructFieldRefs();
        verifyField(structFields.get(0), "device");
        verifyField(structFields.get(1), "browser");
        verifyField(structFields.get(2), "os");
    }

    private void verifyField(StructField structField, String fieldName) {
        assertThat(structField)
                .returns(fieldName, StructField::getFieldName)
                .returns(PrimitiveObjectInspectorFactory.writableStringObjectInspector, StructField::getFieldObjectInspector);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    UserAgentParserUDF tested = new UserAgentParserUDF();
}