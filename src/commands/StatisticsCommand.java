package commands;

import optimization.Optimization;

import org.apache.log4j.Logger;

import parsers.ConfigurationInput;
import datamodel.ExperimentalValue;

/**
 * Command that executes the statistics calculation via
 * the Param_Est object
 * @author nmvdewie
 *
 */
public class StatisticsCommand implements Command {
	public static Logger logger = Logger.getLogger(StatisticsCommand.class);
	ConfigurationInput config;

	public StatisticsCommand(ConfigurationInput config){
		this.config = config;
	}
	public void execute() {
		logger.info("STATISTICS MODE");
		try {
			statistics();
			Command parity = new ParityPlotCommand();
			parity.execute();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void statistics() throws Exception{
		long time = System.currentTimeMillis();

		//check if initial input file is error-free:
		Command checkChemistry = new CheckChemistryFileCommand();
		checkChemistry.execute();

		// take initial guesses from chem.inp file:
		ConfigurationInput.chemistry.getParams().setBeta(ConfigurationInput.chemistry.initialGuess());

		//read experimental data file:
		ExperimentalValue[] experimentalValues = ConfigurationInput.experiments.getExperimentalData(); 

		Optimization optimization = new Optimization(config);

		optimization.calcStatistics();
		//moveOutputFiles();
		long timeTook = (System.currentTimeMillis() - time)/1000;
		logger.info("Time needed for this optimization to finish: (sec) "+timeTook);	    	    
	}

	
}
