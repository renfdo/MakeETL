package br.com.ambev.etl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import br.com.ambev.etl.ETL;
/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) throws IOException {
		System.out.println("..........  args : " + args);
		String output = "output.txt";
		ETL etl = null;
		try {
			if (args == null || args.length == 0) {
				String filename = "C:\\Users\\re036935\\Documents\\GitAmbev\\CommandCenter\\MakeETLX\\classes\\artifacts\\MakeETLX__1__jar\\IN_config_020502.json";
				ETL.preLandingSourcePath ="C:\\Users\\re036935\\Documents\\GitAmbev\\CommandCenter\\MakeETLX\\classes\\artifacts\\MakeETLX__1__jar\\";
				ETL.preLandingDestinyPath = "C:\\Users\\re036935\\Documents\\GitAmbev\\CommandCenter\\MakeETLX\\classes\\artifacts\\MakeETLX__1__jar\\OK";
				etl = new ETL(filename);
			} else {
				etl = new ETL(args[0]);
				output = args[1];
				ETL.preLandingSourcePath = args[2];
				ETL.preLandingDestinyPath = args[3];
			}
		} catch (FileNotFoundException e) {
//			logger.error("Json file not found!!!");
			e.printStackTrace();
		}
		List<List<String>> outPutTalend = null;
		if (etl != null) {
			outPutTalend = etl.start();
		}
		System.out.println("End..." + outPutTalend);

		File teste = new File(output);
		FileWriter x = new FileWriter(teste);

		for (List<String> ha : outPutTalend) {
			x.write(
					ha.get(0)+','+ha.get(1)
			);
		}
		x.close();
	}

}
