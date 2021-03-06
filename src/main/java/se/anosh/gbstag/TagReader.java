package se.anosh.gbstag;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import se.anosh.gbstag.dao.GbsFileImplementation;
import se.anosh.gbstag.domain.GbsTag;
import se.anosh.gbstag.service.GbsManager;
import se.anosh.gbstag.service.GenericService;
/**
 *
 * GBS tag 0.2
 * 
 * Java command-line tool for reading the tags from Game Boy Sound (gbs) files.
 * 
 * GBS-files are sound files containing ripped chiptune music 
 * from Gameboy and Gameboy Colour
 * 
 * @author Anosh D. Ullenius <anosh@anosh.se>
 * code written in 2019. Based on spctool
 */
public class TagReader {
    
    private static final String VERSION ="gbstag version 0.2";
    private static final String ABOUT = "code by A. Ullenius 2019";
    private static final String LICENCE = "Licence: Gnu General Public License - version 3.0 only";
    
    public static void main(String[] args) {
        
        Options options = new Options();
        options.addOption("v", "verbose", false, "verbose output");
        options.addOption("V", "version", false, "print version");
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            
            if (cmd.hasOption("V"))
                printVersionAndCreditsAndExit();
            
            if (cmd.getArgList().isEmpty())
                    throw new ParseException("No arguments");
            
            TagReader demo = new TagReader();
            demo.go(cmd);
        } catch (ParseException ex) {
            formatter.printHelp("gbs <filename>", options);
            System.exit(0);
        }
        
    }
    
    private static void printVersionAndCreditsAndExit() {
        
        System.out.println(VERSION);
        System.out.println(ABOUT);
        System.out.println(LICENCE);
        System.exit(0);
    }
    
    public void go(final CommandLine cmd)  {
        
        String[] fileNames = cmd.getArgs();
        
        for (String file : fileNames) {
            try {
            	
            	GenericService<GbsTag> service = new GbsManager(new GbsFileImplementation(file));
            	GbsTag myFile = service.read();
            	
            	if (cmd.hasOption("v")) { // verbose output
            		System.out.println("Identifier\t : " + myFile.getHeader());
            		System.out.println("Version Number\t : " + myFile.getVersionNumber());
            		
            	}
            	System.out.println("Title\t\t : " + myFile.getTitle());
                System.out.println("Artist(s)\t : " + myFile.getAuthor()); // composers, named 'Author' in the GBS-spec
                System.out.println("Copyright\t : " + myFile.getCopyright());
                System.out.println("Total Songs\t : " + myFile.getNumberOfSongs());
                System.out.println("First Song\t : " + myFile.getFirstSong());
                
                
            } catch (IOException ex) {
                System.out.println("I/O error");
                System.out.println(ex.getMessage());
                System.exit(0);
            }
        } // end of for-each-loop
    }
    
}
