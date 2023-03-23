import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jogamp.vecmath.Color3f;

public class MTLFile {
	public float transparency;
	public float shininess;
	public Color3f ambient;
	public Color3f diffuse;
	public Color3f specular;
	public Color3f emissive;
	
	public MTLFile(String fileName) {
		File mtlFile = new File(fileName.substring(0, fileName.length()-3)+"mtl");
    	Scanner sc = null;
    	
		try { sc = new Scanner(mtlFile); }
		catch (FileNotFoundException e) { e.printStackTrace(); }
    	
    	sc.nextLine(); sc.nextLine(); sc.nextLine(); sc.nextLine(); sc.next();
        shininess = sc.nextFloat();
        sc.nextLine(); sc.next();
        ambient = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat());
        sc.nextLine(); sc.next();
        diffuse = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat());
        sc.nextLine(); sc.next();
        specular = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat());
        sc.nextLine(); sc.next();
        emissive = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat());
        sc.nextLine(); sc.nextLine(); sc.next();
        transparency = sc.nextFloat();
        
        sc.close();
	}
}
