package deskort;

import java.util.HashMap;

public abstract class Commands{
    final static HashMap<String, RequestFunction> COMMANDS_TO_FUNCTIONS = new HashMap<String, RequestFunction>() {{

        put("logs",     new RequestFunction(MessageProcessor::logsRequest,true));
        put("shutdown", new RequestFunction(MessageProcessor::shutdownRequest,true));

        put("join",     new RequestFunction(MessageProcessor::joinRequest,      false));
        put("warp",     new RequestFunction(MessageProcessor::warpRequest,      false));
        put("play",     new RequestFunction(MessageProcessor::playRequest,      false));
        put("stop",     new RequestFunction(MessageProcessor::stopRequest,      false));
        put("leave",    new RequestFunction(MessageProcessor::leaveRequest,     false));
        put("loop",     new RequestFunction(MessageProcessor::loopRequest,      false));
        put("tracks",   new RequestFunction(MessageProcessor::tracksRequest,    false));
        put("regain",   new RequestFunction(MessageProcessor::regain,           false));
        put("yt",       new RequestFunction(MessageProcessor::youtubeRequest,   false));

        put("purge",    new RequestFunction(MessageProcessor::purgeRequest,     false));
        put("uptime",   new RequestFunction(MessageProcessor::uptimeRequest,    false));
        put("help",     new RequestFunction(MessageProcessor::helpRequest,      false));
        put("sig",      new RequestFunction(MessageProcessor::signatureCheck,   true));
        put("mem",      new RequestFunction(MessageProcessor::memoryRequest,   true));

        put("hash",     new RequestFunction(MessageProcessor::stringHashRequest,false));
        put("sha256",   new RequestFunction(MessageProcessor::stringHashRequest,false));
        put("md5",      new RequestFunction(MessageProcessor::stringHashRequest,false));
        put("sha",      new RequestFunction(MessageProcessor::stringHashRequest,false));

    }};

}
