package com.example.localstack

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * lambda function without connecting sqs
 * this handle is deployed as simple-function by `deploy-simple-lambda.sh`
 * */
class SimpleHandler: RequestHandler<Map<String, String>, String> {
    private val logger: Logger = LoggerFactory.getLogger(SimpleHandler::class.java)

    override fun handleRequest(input: Map<String, String>, context: Context): String {
        logger.info("INFO!");
        logger.debug("DEBUG!");
        logger.trace("TRACE!");
        logger.warn("WARN!");
        logger.error("ERROR!");
        val result = input.map { (k, v) ->
            logger.info("$k:$v")
            "$k:$v"
        }.joinToString(separator = ",") { it }
        return "result... $result"
    }
}

/**
 * lambda function with connecting sqs
 * this handle is deployed as sqs-function by `deploy-sqs-lambda.sh`
 * */
class HandlerForSqs: RequestHandler<SQSEvent, String> {
    private val logger: Logger = LoggerFactory.getLogger(HandlerForSqs::class.java)

    override fun handleRequest(event: SQSEvent, context: Context): String {
        logger.info("INFO!");
        logger.debug("DEBUG!");
        logger.trace("TRACE!");
        logger.warn("WARN!");
        logger.error("ERROR!");
        val result = event.records.map { message ->
            logger.info(message.body)
            message.body
        }.joinToString(separator = ",") { it }
        return "result... $result"
    }
}
