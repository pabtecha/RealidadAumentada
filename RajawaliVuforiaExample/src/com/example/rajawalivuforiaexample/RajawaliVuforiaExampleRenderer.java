package com.example.rajawalivuforiaexample;

import java.io.ObjectInputStream;
import java.util.Stack;
import java.util.zip.GZIPInputStream;

import javax.microedition.khronos.opengles.GL10;

import rajawali.Object3D;
import rajawali.SerializedObject3D;
import rajawali.animation.mesh.SkeletalAnimationObject3D;
import rajawali.animation.mesh.SkeletalAnimationSequence;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.methods.DiffuseMethod;
import rajawali.materials.methods.SpecularMethod;
import rajawali.materials.textures.Texture;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.parser.LoaderOBJ;
import rajawali.parser.md5.LoaderMD5Anim;
import rajawali.parser.md5.LoaderMD5Mesh;
import rajawali.util.RajLog;
import rajawali.vuforia.RajawaliVuforiaRenderer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import rajawali.primitives.Line3D;
import rajawali.primitives.Plane;
/* --------------------------- RVA -----------------
 * a�adir los import necesarios
 */
import android.graphics.Paint;


public class RajawaliVuforiaExampleRenderer extends RajawaliVuforiaRenderer {
	private DirectionalLight mLight;
	private SkeletalAnimationObject3D mBob;
	private Object3D mF22;
	private Object3D mAndroid;
	private Object3D mMonkey; /*Espada*/
	private Object3D mCow;
	private Object3D mCara;
	private Object3D mCaballo;
	private Object3D mBombilla;
	/* --------------------RVA ------------------
	 * a�adir todos los atributos para los objetos a visualizar
	 */
	
	private RajawaliVuforiaExampleActivity activity;

	public RajawaliVuforiaExampleRenderer(Context context) {
		super(context);
		activity = (RajawaliVuforiaExampleActivity)context;
	}

	protected void initScene() {
		mLight = new DirectionalLight(.1f, 0, -1.0f);
		mLight.setColor(1.0f, 1.0f, 0.8f);
		mLight.setPower(1);
		
		getCurrentScene().addLight(mLight);
	
		/* --------------------------- RVA -----------------
		 * a�adir para cargar los objetos obj
		 * 
		 * p. ej.
		  LoaderOBJ objParser1 = new LoaderOBJ(mContext.getResources(),
				mTextureManager, R.raw.monkey);		
				
		 */

		
		
		try {
			//
			// -- Load Bob (model by Katsbits
			// http://www.katsbits.com/download/models/)
			//

			LoaderMD5Mesh meshParser = new LoaderMD5Mesh(this,
					R.raw.boblampclean_mesh);
			meshParser.parse();
			mBob = (SkeletalAnimationObject3D) meshParser
					.getParsedAnimationObject();
			mBob.setScale(2);
			
			LoaderMD5Anim animParser = new LoaderMD5Anim("dance", this,
					R.raw.boblampclean_anim);
			animParser.parse();
			mBob.setAnimationSequence((SkeletalAnimationSequence) animParser
					.getParsedAnimationSequence());

			getCurrentScene().addChild(mBob);

			mBob.play();
			mBob.setVisible(false);

			//
			// -- Load F22 (model by KuhnIndustries
			// http://www.blendswap.com/blends/view/67634)
			//

			GZIPInputStream gzi = new GZIPInputStream(mContext.getResources()
					.openRawResource(R.raw.f22));
			ObjectInputStream fis = new ObjectInputStream(gzi);
			SerializedObject3D serializedObj = (SerializedObject3D) fis
					.readObject();
			fis.close();

			mF22 = new Object3D(serializedObj);
			mF22.setScale(30);
			getCurrentScene().addChild(mF22);
			
			Material f22Material = new Material();
			f22Material.enableLighting(true);
			f22Material.setDiffuseMethod(new DiffuseMethod.Lambert());
			f22Material.addTexture(new Texture("f22Texture", R.drawable.f22));
			f22Material.setColorInfluence(0);
			
			mF22.setMaterial(f22Material);
			
			//
			// -- Load Android
			//
			
			gzi = new GZIPInputStream(mContext.getResources()
					.openRawResource(R.raw.android));
			fis = new ObjectInputStream(gzi);
			serializedObj = (SerializedObject3D) fis
					.readObject();
			fis.close();
			
			mAndroid = new Object3D(serializedObj);
			mAndroid.setScale(30);
			getCurrentScene().addChild(mAndroid);
			
			Material androidMaterial = new Material();
			androidMaterial.enableLighting(true);
			androidMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
			androidMaterial.setSpecularMethod(new SpecularMethod.Phong());
			mAndroid.setColor(0x00dd00);
			mAndroid.setMaterial(androidMaterial);
			
			/* --------------------------- RVA -----------------
			 * cargar los objetos a utilizar
			 * 	
			*/
		
			LoaderOBJ objParser1 = new LoaderOBJ(mContext.getResources(),
					mTextureManager, R.raw.swordobj);
					objParser1.parse();
					mMonkey = objParser1.getParsedObject();
					mMonkey.setScale(3);

					Material MMaterial = new Material();
					MMaterial.enableLighting(true);
					MMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
					MMaterial.setSpecularMethod(new SpecularMethod.Phong());
					mMonkey.setColor(0xD3D3D3);
					mMonkey.setMaterial(MMaterial); 
					
					
			LoaderOBJ objParser2 = new LoaderOBJ(mContext.getResources(),
				mTextureManager, R.raw.vaca);
				objParser2.parse();
				mCow = objParser2.getParsedObject();
				mCow.setScale(4);	
				mCow.setColor(0xFFD39B);
				mCow.setMaterial(MMaterial); 
				mMonkey.addChild(mCow);	
					
				
			LoaderOBJ objParser3 = new LoaderOBJ(mContext.getResources(),
					mTextureManager, R.raw.bulb);
					objParser3.parse();
					mCara = objParser3.getParsedObject();
					mCara.setScale(5);	
					mCara.setColor(0xFFFFFF);
					mMonkey.addChild(mCara);	
					
					
				getCurrentScene().addChild(mMonkey);
				
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void foundFrameMarker(int markerId, Vector3 position,
			Quaternion orientation) {
		
		if (markerId == 0) {
			mBob.setVisible(true);
			mBob.setPosition(position);
			mBob.setOrientation(orientation);
		} else if (markerId == 1) {
			mAndroid.setVisible(true);
			mAndroid.setPosition(position);
			mAndroid.setOrientation(orientation);
		}
	}
	
	
	@Override
	protected void foundImageMarker(String trackableName, Vector3 position,
			Quaternion orientation) {
		if(trackableName.equals("guernica"))
		{
		/*	mBob.setVisible(true);
			mBob.setPosition(position);
			mBob.setOrientation(orientation);*/
			
			mMonkey.setVisible(true);
			mMonkey.setPosition(position);
			mMonkey.setOrientation(orientation);
			
			mCow.setVisible(true);
			mCow.setPosition(-30, -15, 0);
			mCow.setOrientation(orientation);
			
			mCara.setVisible(true);
			mCara.setPosition(30, -50, 5);
			mCara.setOrientation(orientation);
			
			RajLog.d(activity.getMetadataNative());
		}
		/*if(trackableName.equals("stones"))
		{
			mF22.setVisible(true);
			mF22.setPosition(position);
			mF22.setOrientation(orientation);
		}
		*/
		  if(trackableName.equals("chips"))
		{
			mAndroid.setVisible(true);
			mAndroid.setPosition(position);
			mAndroid.setOrientation(orientation);
			RajLog.d(activity.getMetadataNative());

		}
		/* --------------------------- RVA -----------------
		 * a�adir las im�genes objetivo a reconocer
		 * p. ej.
		  if(trackableName.equals("tarmac"))
		{
			mAndroid.setVisible(true);
			mAndroid.setPosition(position);
			mAndroid.setOrientation(orientation);
			RajLog.d(activity.getMetadataNative());

		}
				
		 */
		
		// -- also handle cylinder targets here
		// -- also handle multi-targets here
	}

	@Override
	public void noFrameMarkersFound() {
	}

	public void onDrawFrame(GL10 glUnused) {
		mBob.setVisible(false);
		mF22.setVisible(false);
		mAndroid.setVisible(false);
		mMonkey.setVisible(false);
		mCow.setVisible(false);
		mCara.setVisible(false);
		/* --------------------------- RVA -----------------
		 * realiazar la misma operaci�n para todos los objetos creados
		 * 				
		 */
		
		
		super.onDrawFrame(glUnused);
		
		if (!activity.getScanningModeNative())
		{
			activity.showStartScanButton();
		}
	}
}
