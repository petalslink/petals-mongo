package org.ow2.petals.log.mongo;

import java.util.logging.LogRecord;

/**
 * chamerling - chamerling@linagora.com
 */
public class MongoPayloadDumperHandler extends MongoHandler {

    public static final String DUMP_COLLECTION  ="dumpcollection";
    public static final String DEFAULT_DUMP_COLLECTION  ="dumplogs";

    public MongoPayloadDumperHandler() {
        super();
    }

    /**
     * Use a special collection for this logger
     */
    public void doConfigure() {
        super.doConfigure();
        this.collectionName = getProperty(DUMP_COLLECTION, DEFAULT_DUMP_COLLECTION);
    }

    @Override
    public synchronized void publish(final LogRecord record) {
        super.publish(record);

        // TODO : Get the petals dependency and dump message to record!

        /*
        final Object[] logParameters = record.getParameters();
        if ((logParameters != null) && (logParameters.length != 0) && logParameters[0] != null
                && logParameters[0] instanceof LogData) {

            final LogData logData = (LogData) logParameters[0];
            final String flowStepId = (String) logData
                    .get(PetalsExecutionContext.FLOW_STEP_ID_PROPERTY_NAME);
            final MessageExchange exchange = (MessageExchange) logData
                    .get(PetalsExecutionContext.FLOW_EXCHANGE_PROPERTY_NAME);
            if (exchange != null) {
                final NormalizedMessage outNm = exchange.getMessage(MessageExchangeImpl.OUT_MSG);
                final NormalizedMessage inNm = exchange.getMessage(MessageExchangeImpl.IN_MSG);
                final NormalizedMessage faultNm = exchange.getFault();

                final Source source =
                        faultNm != null ? faultNm.getContent() :
                                outNm != null ? outNm.getContent() :
                                        inNm != null ? inNm.getContent() : null;

                if (source != null) {
                    final TraceCode traceCode = (TraceCode) logData
                            .get(FlowLogData.FLOW_STEP_TRACE_CODE);

                    final File dumpFile = this.getDumpFile(traceCode, flowStepId);
                    logData.putData(PAYLOAD_CONTENT_DUMP_FILE_LOGDATA_NAME,
                            FileSystemHelper.getRelativePath(this.basedir, dumpFile));
                    dumpFile.getParentFile().mkdirs();

                    try {
                        Source faultMessageContentAsSource = SourceHelper.fork(source);
                        SourceHelper.toFile(faultMessageContentAsSource, dumpFile);
                    } catch (final IOException e) {
                        reportError(e.getMessage(), e, ErrorManager.WRITE_FAILURE);
                    } catch (final TransformerException e) {
                        reportError(e.getMessage(), e, ErrorManager.WRITE_FAILURE);
                    }
                }
            }
        }
        */


    }
}
