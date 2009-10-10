package org.gatherdata.archiver.command.internal;

import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.felix.shell.Command;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverService;
import org.gatherdata.commons.net.CbidFactory;
import org.joda.time.DateTime;

import com.google.inject.Inject;

public class ArchiverCommandImpl implements Command {

    public static final String COMMAND_NAME = "archive";

    private final Pattern commandPattern = Pattern.compile("^(\\w+)\\s*(\\w+)\\s*(.*)");

    private final Pattern subCommandPattern = Pattern.compile("^(\\S+)\\s+(.*)");

    @Inject
    ArchiverService archiverService;

    private int mockContentCounter = 0;

    public void execute(String argString, PrintStream out, PrintStream err) {
        Matcher argMatcher = commandPattern.matcher(argString);
        if (argMatcher.matches()) {
            String subCommand = argMatcher.group(2);
            String subArguments = argMatcher.group(3);

            if (archiverService == null) {
                err.println("ArchiverService not available");
                return;
            }

            if ("help".equals(subCommand)) {
                out.println("subcommands: list, content, meta, mock");
                out.println("\tlist - show saved archives");
                out.println("\tcontent <uid> - show the content of archive <uid>");
                out.println("\tmeta <uid> - show the metaata for archive <uid>");
                out.println("\tmock - generate and save mock data");
            } else if ("mock".equals(subCommand)) {
                GatherArchive mockEntity = createMockEntity();
                archiverService.save(mockEntity);
            } else if ("list".equals(subCommand)) {
                for (GatherArchive savedArchive : archiverService.getAll()) {
                    out.println(savedArchive);
                }
            } else if ("content".equals(subCommand)) {
                URI requestedUid = null;
                try {
                    requestedUid = new URI(subArguments);
                    GatherArchive requestedArchive = archiverService.get(requestedUid);
                    if (requestedArchive != null) {
                        out.println(requestedArchive.getContent());
                    } else {
                        err.println("Requested archive not found.");
                    }
                } catch (URISyntaxException e) {
                    err.println("Bad archive uid: " + subArguments);
                }
                
            } else if ("meta".equals(subCommand)) {
                URI requestedUid = null;
                try {
                    requestedUid = new URI(subArguments);
                    GatherArchive requestedArchive = archiverService.get(requestedUid);
                    if (requestedArchive != null) {
                        Map<String, String> metadata = requestedArchive.getMetadata();
                        if (metadata != null) {
                            for (String key : metadata.keySet()) {
                                System.out.println(key  + " : " + metadata.get(key));
                            }
                        } else {
                            System.out.println("No metadata for " + requestedUid);
                        }
                    } else {
                        err.println("Requested archive not found.");
                    }
                } catch (URISyntaxException e) {
                    err.println("Bad archive uid: " + subArguments);
                }
                
            } else {
                err.println("Don't know how to respond to " + subCommand);
            }
        } else {
            err.println("Sorry, can't parse:" + argString);
        }
    }

    private GatherArchive createMockEntity() {
        MutableGatherArchive mockArchive = new MutableGatherArchive();
        String mockContent = "<mock id=\"" + mockContentCounter
                + "\">\n\t<message>real data uses pointy brackets</message>\n</mock>";
        mockArchive.setContent(mockContent);
        mockArchive.setDateCreated(new DateTime());
        mockArchive.setUid(CbidFactory.createCbid(mockContent));
        mockArchive.getMetadata().put("mock-origin", "dropped from sky");
        mockArchive.getMetadata().put("mock-type", "well, it is not flattery");
        mockContentCounter++;
        return mockArchive;
    }

    public String getName() {
        return COMMAND_NAME;
    }

    public String getShortDescription() {
        return "interacts with the ArchiverService";
    }

    public String getUsage() {
        return "archiver <sub-command>";
    }

}
