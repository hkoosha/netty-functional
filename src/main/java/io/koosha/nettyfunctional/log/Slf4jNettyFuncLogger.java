package io.koosha.nettyfunctional.log;


public final class Slf4jNettyFuncLogger implements NettyFuncLogger {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Slf4jNettyFuncLogger.class);

    @Override
    public void error(String msg, Throwable t) {
        log.error(msg, SerrLogger.causeMsg(t));
    }

    @Override
    public void warn(String msg, Throwable t) {
        log.warn(msg, SerrLogger.causeMsg(t));
    }

}
