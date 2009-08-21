package org.gatherdata.archiver.command.internal;

import java.io.PrintStream;

import org.apache.felix.shell.Command;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverService;
import org.gatherdata.commons.net.CbidFactory;
import org.joda.time.DateTime;

import com.google.inject.Inject;

public class ArchiverCommandImpl implements Command {
    
    public static final String COMMAND_NAME = "archiver";
    
    @Inject
    ArchiverService archiverService;

    private int mockContentCounter = 0;

    public void execute(String argString, PrintStream out, PrintStream err) {
        GatherArchive mockEntity = createMockEntity();
        archiverService.save(mockEntity);
    }

    private GatherArchive createMockEntity() {
        MutableGatherArchive mockArchive = new MutableGatherArchive();
        String mockContent = "mock content #" + mockContentCounter ++;
        mockArchive.setContent(mockContent);
        mockArchive.setDateCreated(new DateTime());
        mockArchive.setUid(CbidFactory.createCbid(mockContent));
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
