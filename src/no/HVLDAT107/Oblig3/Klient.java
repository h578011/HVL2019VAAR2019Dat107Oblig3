package no.HVLDAT107.Oblig3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import no.HVLDAT107.Oblig3.Commands.*;


public class Klient {
	
	public static boolean running = true;

	public static void main(String[] args) {
		EclipseTools.fixConsole();//Adds a delay between System.err and System.out prints automagically to prevent inconsistent prints
		MenuProgram();
	}
	
	//runs until completely exited
	public static void MenuProgram(){
		
		Commands.input = new Scanner(System.in);
		
		//main program loop, if a menu tree ends, standard menu gets shown again, 
		//can be exited through the running boolean.
		while(running) {
			EnterStandardMenu();
			
		}
		
		Commands.input.close();
	}
	
	public static void EnterStandardMenu() {
		Command[] StartMenu = {
				new Commands.Command("an", "Enter ansatt menu", Klient::EnterAnsattMenu),
				new Commands.Command("av", "Enter Avdelings menu", Klient::EnterAvdelingsMenu),
				new Commands.Command("pr", "Enter Prosjekt menu", Klient::EnterProsjektMeny),
				new Commands.Command("cs", "Create sample data for all tables", Klient::CreateAllSampleData),
				new Commands.Command("pa","Print out all tables" ,Klient::PrintAllTables),
				new Commands.Command("r","Reset Entire DataBase",  DataBase::ResetSchemaAndTables),
				new Commands.Command("e", "Exit", Klient :: Exit)
				
		};
		Commands.showCommands(StartMenu);
	}
	
	public static void Exit() {
		running = false;
		System.err.println("Program shutdown.");
	}
	
	public static void PrintAllTables() {
		System.err.println("Ansatte");
		List<Ansatt> ansatte = AnsattEAO.hentAlleAnsatte();
		ansatte.remove(0);
	
		Helper.PrintList(ansatte);
		
		System.err.println("Avdelinge");
		List<Avdeling> Avdelinge = AvdelingEAO.hentAlleAvdelinge();
		Avdelinge.remove(0);
	
		Helper.PrintList(Avdelinge);
		
		System.err.println("Prosjekte");
		List<Prosjekt> Prosjekte = ProsjektEAO.hentAlleProsjekter();
		
	
		Helper.PrintList(Prosjekte);
		
		System.err.println("ProsjektOversikte");
		List<ProsjektOversikt> ProsjektOversikte = ProsjektOversiktEAO.hentAlleProsjektOversikter();
		
	
		Helper.PrintList(ProsjektOversikte);
	}
	
	//creates sample data in all tables, sets up 3 Avdeling's,
	//6 Ansatt + 3 Bosses, 3 projects & 3 workOnProject logs
	public static void CreateAllSampleData() {
		
		List<Avdeling> samplesAv = new ArrayList<Avdeling>();
		samplesAv.add(new Avdeling("venstre",1));
		samplesAv.add(new Avdeling("hoyre",1));
		samplesAv.add(new Avdeling("midt",1));
		
		for (Avdeling a : samplesAv) {
			AvdelingEAO.leggTilAvdeling(a);
		}
		
		System.err.println("Created Avdeling samples");
		
		
		List<Ansatt> samplesAn = new ArrayList<Ansatt>();
		samplesAn.add(new Ansatt("Bos1","one","venstre",java.sql.Date.valueOf(LocalDate.now()), "Boss",12331231,2 ));
		samplesAn.add(new Ansatt("Bos2","three","hoyre",java.sql.Date.valueOf(LocalDate.now()), "Boss",12331231,3 ));
		samplesAn.add(new Ansatt("Bos3","five","midt",java.sql.Date.valueOf(LocalDate.now()), "Boss",12331231,4 ));
		
		for (Ansatt a : samplesAn) {
			AnsattEAO.leggTilAnsatt(a);
		}
		System.err.println("Created Boss Ansatt samples");
		
		
		int i = 2;
		
		for (Avdeling a : samplesAv) {
			AvdelingEAO.EditAvdelingSjef(a, i);
			i++;
		}
		System.err.println("Reasigned Avdeling bosses");
		
		
		List<Ansatt> samples = new ArrayList<Ansatt>();
		
		samples.add(new Ansatt("aadd","six","seven",java.sql.Date.valueOf(LocalDate.now()), "employee",12331231,2 ));
		samples.add(new Ansatt("aaee","eight","nine",java.sql.Date.valueOf(LocalDate.now()), "employee",12331231,2 ));
		samples.add(new Ansatt("bbaa","ten","eleven",java.sql.Date.valueOf(LocalDate.now()), "employee",12331231,3 ));
		samples.add(new Ansatt("bbbb","twelve","thirteen",java.sql.Date.valueOf(LocalDate.now()), "employee",12331231,3 ));
		samples.add(new Ansatt("bbcc","fourteen","fifteen",java.sql.Date.valueOf(LocalDate.now()), "employee",12331231,4 ));
		samples.add(new Ansatt("bbdd","sixteen","seventeen",java.sql.Date.valueOf(LocalDate.now()), "employee",12331231,4 ));
		
		for (Ansatt a : samples) {
			AnsattEAO.leggTilAnsatt(a);
		}
		
		System.err.println("Added more Ansatt samples");
		

		List<Prosjekt> samplesProsjekt = new ArrayList<Prosjekt>();
		samplesProsjekt.add(new Prosjekt("CleanStreets","Clean the streets"));
		samplesProsjekt.add(new Prosjekt("Music","making music"));
		samplesProsjekt.add(new Prosjekt("dogpark","making a dog park"));
		
		for (Prosjekt a : samplesProsjekt) {
			ProsjektEAO.leggTilProsjekt(a);
		}
		
		System.err.println("Created Prosjekt samples");
		
		List<ProsjektOversikt> samplesProsjektOversikt = new ArrayList<ProsjektOversikt>();
		samplesProsjektOversikt.add(new ProsjektOversikt(4,1,"Cleaner",413));
		samplesProsjektOversikt.add(new ProsjektOversikt(3,2,"Musician",4123));
		samplesProsjektOversikt.add(new ProsjektOversikt(6,3,"walker",12));
		
		for (ProsjektOversikt a : samplesProsjektOversikt) {
			ProsjektOversiktEAO.leggTilProsjektOversikt(a);
		}
		
		System.err.println("Created ProsjektOversikt samples");
		
	}
	
	///////     Prosjekt Menyer     ////////////////////////
	
	static Prosjekt currentProsjekt;

	public static void EnterProsjektMeny() {
		Command[] ProsjektMenu ={
				new Commands.Command("cp", "Create Prosjekt", Prosjekt::CreateProsjekt),
				new Commands.Command("s", "Search for Prosjekt menu", Klient::SearchForProsjekt),
				new Commands.Command("p", "Show list of Prosjekt", Prosjekt :: SkrivUtAlleProsjekter),
				//new Commands.Command("cl","//create sample list of ansatte",  Klient::CreateSampleAnsatte),
				new Commands.Command("e", "Exit to Standard Menu", Klient :: EnterStandardMenu)		
		};
		
		Commands.showCommands(ProsjektMenu);
	}
	
	public static void SearchForProsjekt() {
		Command[] SearchForAnsattMenu ={
				new Commands.Command("i", "Search for Prosjekt by id", Klient::SearchForProsjektByID),	
				new Commands.Command("u", "Search for Prosjekt by name", Klient::SearchForProsjektByName),
				new Commands.Command("e", "Exit", Klient::EnterProsjektMeny)
		};
		
		Commands.showCommands(SearchForAnsattMenu);
	}
	
	public static void SearchForProsjektByID() {
		int searchID = Helper.InputInt("Input id(int):");
		System.err.println("Result:");
		currentProsjekt = ProsjektEAO.findProsjektByID(searchID);
		System.out.println(currentProsjekt);
		
		if(currentProsjekt != null) {
			EditProsjekt();
		} else {
			SearchForProsjekt();
		}
	}
	
	public static void SearchForProsjektByName() {
		String searchString = Helper.InputString("Input name:");
		System.err.println("Result:");
		currentProsjekt = ProsjektEAO.findProsjektByName(searchString);
		System.out.println(currentProsjekt);
		if(currentProsjekt != null) {
			EditProsjekt();
		} else {
			SearchForProsjekt();
		}
	}
	
	public static void EditProsjekt() {
		Command[] EditProsjektMenu ={
				new Commands.Command("n", "edit navn", Klient::EditProsjektNavn),	
				new Commands.Command("b", "edit beskrivelse", Klient::EditProsjektBeskrivelse),
				new Commands.Command("rd", "Registrer Prosjekt deltagelse", Klient::EditProsjektRegistrerAnsattDeltagelse),
				new Commands.Command("to", "Skriv ut timeoversikt:", Klient::EditProsjektSkrivUtTimeOversikt),
				new Commands.Command("ok", "Skriv ut oversiktkort", Klient:: EditProsjektSkrivUtTimeOversiktShort),
				new Commands.Command("d", "slett Prosjekt", Klient::SlettProsjekt),
				new Commands.Command("e", "Exit", Klient::EnterAnsattMenu)
		};
		
		Commands.showCommands(EditProsjektMenu);
		
	}
	
	public static void EditProsjektSkrivUtTimeOversikt() {
			
			System.err.println("Timeoversikt:");
			List<ProsjektOversikt> oversikter = ProsjektOversiktEAO.hentAlleProsjektOversikter();
			
			for(int i = oversikter.size()-1; i >= 0; i--) {
				if(oversikter.get(i).getProsjektID() != currentProsjekt.getId()) {
					oversikter.remove(i);
				}
			}
			
			
			Helper.PrintList(oversikter);
			
			EditProsjekt();
		}
	
	public static void EditProsjektSkrivUtTimeOversiktShort() {
		
		//local to function helper class
		final class Oversikt{
			public String ansattNavn;
			public int id;
			public int timer;
			public List<String> roller = new ArrayList<String>();
			
			public Oversikt(int id,String navn, int timer, String rolle) {
				this.id = id;
				ansattNavn = navn;
				this.timer = timer;
				roller.add(rolle);
			}
			
			public String toString() {
				String rolle = "";
				for(String r : roller) {
					if(rolle == "") {
						rolle += r ;
					} else {
						rolle += ", "+r ;
					}
				}
				return ansattNavn + "    timer:" + timer + "    Rolle(r):" + rolle;
			}
		}
		
		System.err.println("Timeoversikt:");
		List<ProsjektOversikt> oversikter = ProsjektOversiktEAO.hentAlleProsjektOversikter();
		
		List<Oversikt> oversikterKort = new ArrayList<Oversikt>();
		
		for(int i = oversikter.size()-1; i >= 0; i--) {
			
			ProsjektOversikt thisOversikt = oversikter.get(i);
			if(thisOversikt.getProsjektID() != currentProsjekt.getId()) {
				oversikter.remove(i);
			} else {
				boolean added = false;
				for(int u = 0; u < oversikterKort.size(); u++) {
					Oversikt thisKort = oversikterKort.get(u);
					if(thisKort.id == thisOversikt.getAnsattID() ) {
						thisKort.timer += thisOversikt.getAnsattArbeidsTimer();
						thisKort.roller.add(thisOversikt.getAnsattRolle());
						added = true;
						break;
					}
				}
				
				if(!added) {
					oversikterKort.add(new Oversikt(thisOversikt.getAnsattID(),
							AnsattEAO.findAnsattByID( thisOversikt.getAnsattID()).getBrukernavn(),
							thisOversikt.getAnsattArbeidsTimer(),thisOversikt.getAnsattRolle()));
				}
			}
		}
		
		int totalTimer = 0;
		for (Oversikt o : oversikterKort) {
			System.out.println(o);
			totalTimer += o.timer;
		}
		System.out.println("Total timer: " + totalTimer);
		
		EditProsjekt();
	}

	public static void EditProsjektRegistrerAnsattDeltagelse() {
		System.err.println("Registrer prosjekt deltagelse for " +currentProsjekt.getNavn()+ ":");
		
		ProsjektOversikt.CreateProsjektOversiktForProsjekt(currentProsjekt.getId());
		
		System.out.println(currentProsjekt);
		EditProsjekt();
	}
	
	public static void EditProsjektNavn() {
		String nyStilling = Helper.InputString("Nytt navn:");
		
		currentProsjekt= ProsjektEAO.EditProsjektNavn(currentProsjekt, nyStilling);
		System.out.println(currentProsjekt);
		EditProsjekt();
	}
	
	public static void EditProsjektBeskrivelse() {
		String nyLonn = Helper.InputString("Ny beskrivelse:");
		
		currentProsjekt = ProsjektEAO.EditProsjektBeskrivelse(currentProsjekt, nyLonn);
		System.out.println(currentProsjekt);
		EditProsjekt();
	}
	
	public static void SlettProsjekt() {
			
			System.out.println("Sletter Prosjekt");
			if(Helper.confirmation()) {
				ProsjektEAO.DeleteProsjekt(currentProsjekt);
				currentProsjekt = null;
				SearchForProsjekt();
			} else {
				System.out.println(currentProsjekt);
				EditAvdeling();
			}
		}
	
	///////     Ansatt Menyer      ////////////////////////
	static Ansatt currentAnsatt;

	public static void EnterAnsattMenu() {
		Command[] AnsattMenu ={
				new Commands.Command("ca", "Create Ansatt", Ansatt::CreateAnsatt),
				new Commands.Command("s", "Search for Ansatt menu", Klient::SearchForAnsatt),
				new Commands.Command("p", "Show list of Ansatte", Ansatt :: SkrivUtAlleAnsatte),
				//new Commands.Command("cl","//create sample list of ansatte",  Klient::CreateSampleAnsatte),
				new Commands.Command("e", "Exit to Standard Menu", Klient :: EnterStandardMenu)		
		};
		
		Commands.showCommands(AnsattMenu);
	}
	
	public static void SearchForAnsatt() {
		Command[] SearchForAnsattMenu ={
				new Commands.Command("i", "Search for Ansatt by id", Klient::SearchForAnsattByID),	
				new Commands.Command("u", "Search for Ansatt by username", Klient::SearchForAnsattByUserName),
				new Commands.Command("e", "Exit", Klient::EnterAnsattMenu)
		};
		
		Commands.showCommands(SearchForAnsattMenu);
	}
	
	public static void SearchForAnsattByID() {
		int searchID = Helper.InputInt("Input id(int):");
		System.err.println("Result:");
		currentAnsatt = AnsattEAO.findAnsattByID(searchID);
		System.out.println(currentAnsatt);
		
		if(currentAnsatt != null) {
			EditAnsatt();
		} else {
			SearchForAnsatt();
		}
	}
	
	public static void SearchForAnsattByUserName() {
		String searchString = Helper.InputString("Input username:");
		System.err.println("Result:");
		currentAnsatt = AnsattEAO.findAnsattByUserName(searchString);
		System.out.println(currentAnsatt);
		if(currentAnsatt != null) {
			EditAnsatt();
		} else {
			SearchForAnsatt();
		}
	}
	
	public static void EditAnsatt() {
		Command[] EditAnsattMenu ={
				new Commands.Command("rp", "Registrer Prosjekt deltagelse", Klient::EditAnsattRegistrerProsjektDeltagelse),
				new Commands.Command("po", "Skriv ut timeoversikt", Klient::EditAnsattSkrivUtTimeOversikt),
				new Commands.Command("s", "edit stilling", Klient::EditAnsattStilling),	
				new Commands.Command("l", "edit l�nn", Klient::EditAnsattLonn),
				new Commands.Command("a", "edit avdeling", Klient::EditAnsattAvdeling),
				new Commands.Command("d", "slett Ansatt", Klient::SlettAnsatt),
				new Commands.Command("e", "Exit", Klient::EnterAnsattMenu)
		};
		
		Commands.showCommands(EditAnsattMenu);
		
	}
	
	public static void EditAnsattSkrivUtTimeOversikt() {
		
		System.err.println("Timeoversikt:");
		List<ProsjektOversikt> oversikter = ProsjektOversiktEAO.hentAlleProsjektOversikter();
		
		for(int i = oversikter.size()-1; i >= 0; i--) {
			if(oversikter.get(i).getAnsattID() != currentAnsatt.getId()) {
				oversikter.remove(i);
			}
		}
		
		Helper.PrintList(oversikter);
	}
	
	public static void EditAnsattRegistrerProsjektDeltagelse() {
		System.err.println("Registrer prosjekt deltagelse for " +currentAnsatt.getBrukernavn() + ":");
		
		ProsjektOversikt.CreateProsjektOversiktForAnsatt(currentAnsatt.getId());
		
		System.out.println(currentAnsatt);
		EditAnsatt();
	}
	
	public static void EditAnsattStilling() {
		String nyStilling = Helper.InputString("Ny Stilling:");
		
		currentAnsatt= AnsattEAO.EditAnsattStilling(currentAnsatt, nyStilling);
		System.out.println(currentAnsatt);
		EditAnsatt();
	}
	
	public static void EditAnsattLonn() {
		int nyLonn = Helper.InputInt("Ny L�nn:");
		
		currentAnsatt = AnsattEAO.EditAnsattLonn(currentAnsatt, nyLonn);
		System.out.println(currentAnsatt);
		EditAnsatt();
	}
	
	public static void EditAnsattAvdeling() {
		if(AvdelingEAO.findAvdelingByID(currentAnsatt.getAvdeling()).getSjefID() == currentAnsatt.getId()) {
			System.err.println("Ansatt er Sjef, kan ikke bytte avdeling!");
			EditAnsatt();
		} else {
			
			int avdeling = -1;
			List<Avdeling> avdelinger = AvdelingEAO.hentAlleAvdelinge();
	    	avdelinger.remove(0);
	    	
	    	List<Integer> avde = new ArrayList<Integer>();
	    	
	    	for(int i = 0; i < avdelinger.size(); i++) {
	    		avde.add(avdelinger.get(i).getId());
	    	}
	    	
	    	while (avdeling== -1) {
	    		System.out.println("Printer avdelinger:");
	    		Helper.PrintList(avdelinger);
	    		
	    		int avd = Helper.InputInt("Input Avdeling:");
	    		
	    		if (avde.contains(avd)){
	    			avdeling = avd;
	    		} else {
	    			System.out.println("Avdelingen finnes ikke");
	    		}
	    		
	    	}
			currentAnsatt= AnsattEAO.EditAnsattAvdeling(currentAnsatt, avdeling);
			System.out.println(currentAnsatt);
			EditAnsatt();
		}
	}
	
	public static void SlettAnsatt() {
		
		if(AvdelingEAO.findAvdelingByID(currentAnsatt.getAvdeling()).getSjefID() == currentAnsatt.getId()) {
			System.err.println("Ansatt er Sjef, kan ikke slettes!");
			EditAnsatt();
		} else {
			System.out.println("Sletter Ansatt");
			if(Helper.confirmation()) {
				AnsattEAO.DeleteAnsatt(currentAnsatt);
				currentAnsatt= null;
				SearchForAnsatt();
			} else {
				System.out.println(currentAnsatt);
				EditAnsatt();
			}
		}
		
	}
	
	/*
	public static void CreateSampleAnsatte() {
		List<Ansatt> samples = new ArrayList<Ansatt>();
		samples.add(new Ansatt("aaaa","one","two",java.sql.Date.valueOf(LocalDate.now()), "dude",12331231,1 ));
		samples.add(new Ansatt("aabb","three","four",java.sql.Date.valueOf(LocalDate.now()), "dude",12331231,1 ));
		samples.add(new Ansatt("aacc","five","March",java.sql.Date.valueOf(LocalDate.now()), "dude",12331231,1 ));
		samples.add(new Ansatt("aadd","six","seven",java.sql.Date.valueOf(LocalDate.now()), "dude",12331231,1 ));
		samples.add(new Ansatt("aaee","eight","nine",java.sql.Date.valueOf(LocalDate.now()), "dude",12331231,1 ));
		samples.add(new Ansatt("bbaa","ten","eleven",java.sql.Date.valueOf(LocalDate.now()), "Boss",12331231,1 ));
		samples.add(new Ansatt("bbbb","twelve","thirteen",java.sql.Date.valueOf(LocalDate.now()), "dude",12331231,1 ));
		samples.add(new Ansatt("bbcc","fourteen","fifteen",java.sql.Date.valueOf(LocalDate.now()), "dude",12331231,1 ));
		samples.add(new Ansatt("bbdd","sixteen","seventeen",java.sql.Date.valueOf(LocalDate.now()), "dude",12331231,1 ));
		
		
		for (Ansatt a : samples) {
			AnsattEAO.leggTilAnsatt(a);
		}
		
		System.err.println("Created samples");
	}
	*/
	////////////  Avdelings Menyer    ///////////////
	
	static Avdeling currentAvdeling;
	
	public static void EnterAvdelingsMenu() {
		Command[] AnsattMenu ={
				new Commands.Command("ca", "Create Avdeling", Avdeling::CreateAvdeling),
				new Commands.Command("s", "Search for Avdeling menu", Klient::SearchForAvdeling),
				new Commands.Command("l", "Show list of Avdeling", Avdeling :: SkrivUtAlleAvdelinge),
				new Commands.Command("la", "Show list of Avdeling m/ ansatte", Avdeling :: SkrivUtAlleAvdelingeMedAnsatte),
				//new Commands.Command("cl","//create sample list of Avdeling",  Klient::CreateSampleAvdeling),
				new Commands.Command("e", "Exit to Standard Menu", Klient :: EnterStandardMenu)		
		};
		
		Commands.showCommands(AnsattMenu);
	}
	
	public static void SearchForAvdeling() {
		Command[] SearchForAnsattMenu ={
				new Commands.Command("i", "Search for Ansatt by id", Klient::SearchForAnsattByID),	
				new Commands.Command("u", "Search for Ansatt by username", Klient::SearchForAnsattByUserName),
				new Commands.Command("e", "Exit", Klient::EnterAnsattMenu)
		};
		
		Commands.showCommands(SearchForAnsattMenu);
	}
	
	public static void SearchForAvdelingByID() {
		int searchID = Helper.InputInt("Input id(int):");
		System.err.println("Result:");
		currentAvdeling = AvdelingEAO.findAvdelingByID(searchID);
		System.out.println(currentAvdeling);
		
		if(currentAvdeling != null) {
			EditAvdeling();
		} else {
			SearchForAvdeling();
		}
	}
	
	public static void SearchForAvdelingByName() {
		String searchString = Helper.InputString("Input username:");
		System.err.println("Result:");
		currentAvdeling = AvdelingEAO.findAvdelingByName(searchString);
		System.out.println(currentAvdeling);
		if(currentAvdeling != null) {
			EditAvdeling();
		} else {
			SearchForAvdeling();
		}
	}
	
	public static void EditAvdeling() {
		Command[] EditAvdelingMenu ={
				new Commands.Command("n", "edit navn", Klient::EditAvdelingNavn),	
				new Commands.Command("sj", "edit sjef", Klient::EditAvdelingSjef),
				new Commands.Command("d", "edit delete", Klient::SlettAvdeling),
				new Commands.Command("e", "Exit", Klient::EnterAvdelingsMenu)
		};
		
		Commands.showCommands(EditAvdelingMenu);
		
	}
	
	public static void EditAvdelingNavn() {
		String nyNavn = Helper.InputString("Nyttt navn:");
		
		currentAvdeling= AvdelingEAO.EditAvdelingnavn(currentAvdeling, nyNavn);
		System.out.println(currentAvdeling);
		EditAvdeling();
	}
	
	public static void EditAvdelingSjef() {
		int nySjefID = Helper.InputInt("Ny Sjef:");
		
		currentAvdeling = AvdelingEAO.EditAvdelingSjef(currentAvdeling, nySjefID);
		System.out.println(currentAvdeling);
		EditAvdeling();
	}
	
	public static void SlettAvdeling() {
		
		System.out.println("Sletter Avdeling");
		if(Helper.confirmation()) {
			AvdelingEAO.DeleteAvdeling(currentAvdeling);
			currentAvdeling = null;
			SearchForAvdeling();
		} else {
			System.out.println(currentAvdeling);
			EditAvdeling();
		}
	}
	
	/*
	public static void CreateSampleAvdeling() {
		List<Avdeling> samples = new ArrayList<Avdeling>();
		samples.add(new Avdeling("venstre",1));
		samples.add(new Avdeling("hoyre",1));
		samples.add(new Avdeling("midt",1));
		
		for (Avdeling a : samples) {
			AvdelingEAO.leggTilAvdeling(a);
		}
		
		System.err.println("Created samples");
	}
	*/
}
