import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;

public class MTLFile {
	public float shininess = 0;
	public Color3f ambient = new Color3f();
	public Color3f diffuse = new Color3f();
	public Color3f specular = new Color3f();
	public Color3f emissive = new Color3f();
	public float transparency = 0;
	public Texture texture = null;
	
	public MTLFile(String fileName, int mtlNum) {
		File mtlFile = new File(fileName.substring(0, fileName.length()-3)+"mtl");
		File objFile = new File(fileName.substring(0, fileName.length()-3)+"obj");
    	Scanner sc = null;
		String mtlName = "";
    	
		try { sc = new Scanner(objFile); }
		catch (FileNotFoundException e) { e.printStackTrace(); }

		while(mtlNum >= 0) {
			sc.nextLine();
			if(sc.next().equals("usemtl")) 
				mtlNum--;
		}
		mtlName = sc.next();
		sc.close();

		try { sc = new Scanner(mtlFile); }
		catch (FileNotFoundException e) { e.printStackTrace(); }

		while(!mtlName.equals(sc.next())) {
			sc.nextLine();
			sc.next();
		}
		
		while(sc.hasNext()) {
			switch(sc.next()) {
			case "Ns": shininess = sc.nextFloat(); continue;
			case "Ka": ambient = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat()); continue;
			case "Kd": diffuse = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat()); continue;
			case "Ks": specular = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat()); continue;
			case "Ke": emissive = new Color3f(sc.nextFloat(),sc.nextFloat(),sc.nextFloat()); continue;
			case "d" : transparency = sc.nextFloat(); continue;
			case "map_Kd": texture = getTexture(sc.next()); continue;
			case "newmtl": break;
			default: continue;
			}
			break;
		}

        sc.close();

		/*
        System.out.println(fileName);
        System.out.println(shininess);
        System.out.println(ambient);
        System.out.println(diffuse);
        System.out.println(specular);
        System.out.println(emissive);
        System.out.println(transparency);
        System.out.println(texture);
        System.out.println("----------------------------------");
		 */
	}
	
	private static Texture getTexture(String fileName) {
		TextureLoader loader = new TextureLoader("objects/"+fileName, null);
		ImageComponent2D image = loader.getImage();        // load the image
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());

		texture.setImage(0, image);                        // set image for the texture
		return texture;
	}
}
