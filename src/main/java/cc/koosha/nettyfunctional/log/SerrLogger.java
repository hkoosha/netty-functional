package cc.koosha.nettyfunctional.log;

import java.io.PrintWriter;
import java.io.StringWriter;


public final class SerrLogger implements NettyFuncLogger {

    @Override
    public void error(String msg, Throwable t) {
        System.err.println(format("error", msg, t));
    }

    @Override
    public void warn(String msg, Throwable t) {
        System.err.println(format("warn", msg, t));
    }


    static String format(String level, String msg, Throwable t) {
        return "NettyFunctional " + level + ": " + msg + ", " + causeMsg(t);
    }

    static String causeMsg(Throwable cause) {
        if (cause == null)
            return "?";
        else if (cause.getMessage() == null || cause.getMessage().isEmpty())
            return cause.getClass().getSimpleName();
        else
            return cause.getClass().getSimpleName() + ": " + cause.getMessage();
    }

    static String stacktrace(final Throwable t) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        final String stacktrace = pw.toString();
        return sw.toString();
    }

}
