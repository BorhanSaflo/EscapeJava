import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jogamp.vecmath.Color3f;

public class MTLFile {
	public float shininess;
	public Color3f ambient;
	public Color3f diffuse;
	public Color3f specular;
	public Color3f emissive;
	public float transparency;
	
	public MTLFile(String fileName) {
		File mtlFile = new File(fileName.substring(0, fileName.length()-3)+"mtl");
    	Scanner sc = null;
    	
		try { sc = new Scanner(mtlFile); }
		catch (FileNotFoundException e) { e.printStackTrace();}
    	
		for(int i = 0; i < 4; i++)
			sc.nextLine();
		
		while(true) {
			switch(sc.next()) {
			case "Ns": shininess = sc.nextFloat(); continue;
			case "Ka": ambient = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat()); continue;
			case "Kd": diffuse = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat()); continue;
			case "Ks": specular = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat()); continue;
			case "Ke": emissive = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat()); continue;
			case "d" : transparency = sc.nextFloat(); continue;
			case "illum": break;
			default: continue;
			}
			break;
		}

        sc.close();
        
        System.out.println(fileName);
        System.out.println(shininess);
        System.out.println(ambient);
        System.out.println(diffuse);
        System.out.println(specular);
        System.out.println(emissive);
        System.out.println(transparency);
        System.out.println("----------------------------------");
	}
}
