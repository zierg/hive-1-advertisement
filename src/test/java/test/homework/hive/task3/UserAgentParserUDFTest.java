package test.homework.hive.task3;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import homework.hive.task3.UserAgentParserUDF;

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


        exception.expect(UDFArgumentException.class);
        exception.expectMessage("User Agent Parser takes only one argument");
        tested.initialize(new ObjectInspector[] {});
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    UserAgentParserUDF tested = new UserAgentParserUDF();
}