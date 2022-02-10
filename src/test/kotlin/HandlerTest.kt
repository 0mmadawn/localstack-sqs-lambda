import com.amazonaws.services.lambda.runtime.ClientContext
import com.amazonaws.services.lambda.runtime.CognitoIdentity
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.tests.EventLoader
import com.example.localstack.HandlerForSqs
import com.example.localstack.SimpleHandler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class SimpleHandlerTest {
    @Test
    fun handleRequestTest() {
        val input = mapOf(
            "test" to "test value",
            "hoge" to "huga"
        )
        val context = TestContext()
        val result = assertDoesNotThrow {
            // instead of assertion, see logs!
            SimpleHandler().handleRequest(input, context)
        }
        assertEquals("result... test:test value,hoge:huga", result)
    }
}

class HandlerForSqsTest {
    @Test
    fun handleRequestTest() {
        val event = EventLoader.loadSQSEvent("sample-sqs-event.json")
        val context = TestContext()
        val result = assertDoesNotThrow {
            // instead of assertion, see logs!
            HandlerForSqs().handleRequest(event, context)
        }
        // ugly test
        assertEquals(
            "result... This json file is almost copy of https://docs.aws.amazon.com/ja_jp/lambda/latest/dg/with-sqs.html",
            result
        )
    }
}

// mocks
/** [https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/test/java/example]*/
class TestContext : Context {
    override fun getAwsRequestId(): String = "495b12a8-xmpl-4eca-8168-160484189f99"
    override fun getLogGroupName(): String = "/aws/lambda/my-function"
    override fun getLogStreamName(): String = "2020/02/26/[\$LATEST]704f8dxmpla04097b9134246b8438f1a"
    override fun getFunctionName(): String = "my-function"
    override fun getFunctionVersion(): String = "\$LATEST"
    override fun getInvokedFunctionArn(): String = "arn:aws:lambda:us-east-2:123456789012:function:my-function"
    override fun getIdentity(): CognitoIdentity? = null
    override fun getClientContext(): ClientContext? = null
    override fun getRemainingTimeInMillis(): Int = 300000
    override fun getMemoryLimitInMB(): Int = 512
    override fun getLogger(): LambdaLogger = TestLogger()
}
/** [https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic/src/test/java/example]*/
class TestLogger : LambdaLogger {
    override fun log(message: String) = logger.info(message)
    override fun log(message: ByteArray) = logger.info(String(message))
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SimpleHandler::class.java)
    }
}