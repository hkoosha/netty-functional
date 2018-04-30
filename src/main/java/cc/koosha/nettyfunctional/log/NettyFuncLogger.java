package cc.koosha.nettyfunctional.log;


public interface NettyFuncLogger {

    void error(String msg, Throwable t);

    void warn(String msg, Throwable t);

}
