package com.efei.kids.argame;

import android.content.Context;
import android.view.MotionEvent;

import com.efei.kids.argame.data.EulerAngleData;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;

import java.util.Stack;

/**
 * Created by jesse on 15-11-12.
 */
public class Renderer extends RajawaliRenderer {


    public Context context;
    private DirectionalLight directionalLight;
    private Sphere earthSphere;
    private Object3D sphere;
    private Object3D[] sphere_ary = new Object3D[4];

    public Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(30.0);
    }

    private PointLight mLight;
    private Object3D[] mObjects;
    private Animation3D mCameraAnim;

    private float[][] mPosition;

    long mTime;
    int mLookAtIndex;

    private EulerAngleData data;

    public void initScene(){

        mLight = new PointLight();
        mLight.setPosition(0, 0, 0);
        mLight.setPower(20);
        getCurrentScene().addLight(mLight);

        int thick = 3;
        int length = 5;

        Stack<Vector3> l1 = new Stack<>();
        l1.add(0, new Vector3(0, 0, -20));
        l1.add(1, new Vector3(length, 0, -20));

        Stack<Vector3> l2 = new Stack<>();
        l2.add(0, new Vector3(0, 0, -20));
        l2.add(1, new Vector3(0, length, -20));

        Stack<Vector3> l3 = new Stack<>();
        l3.add(0, new Vector3(0, 0, -20));
        l3.add(1, new Vector3(0, 0, -20 + length));

        Line3D x_axis = new Line3D(l1, thick, 0xFF0000);
        Line3D y_axis = new Line3D(l2, thick, 0x00FF00);
        Line3D z_axis = new Line3D(l3, thick, 0x0000FF);

        Material x_axis_m = new Material();
        x_axis_m.setColor(0xFF0000);
        x_axis.setMaterial(x_axis_m);

        Material y_axis_m = new Material();
        y_axis_m.setColor(0xFF0000);
        y_axis.setMaterial(y_axis_m);

        Material z_axis_m = new Material();
        z_axis_m.setColor(0xFF0000);
        z_axis.setMaterial(z_axis_m);

        getCurrentScene().addChild(x_axis);
        getCurrentScene().addChild(y_axis);
        getCurrentScene().addChild(z_axis);

        mObjects = new Object3D[6];
        mTime = -5000000000L;
        mLookAtIndex = 0;

        try {

            mPosition = new float[][] {
                    {20, 0, 0},
                    {-20, 0, 0},
                    {0, 20, 0},
                    {0, -20, 0},
                    {0, 0, 20},
                    {0, 0, -20}
            };

            int[] colors = new int[] { 0xFF0000, 0x00FF00, 0x0000FF, 0x999900, 0x990099, 0x009999 };
            Material[] materials = new Material[6];

            for (int i = 0; i < 6; i++) {
                LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),
                        mTextureManager, R.raw.monkey_obj);
                objParser.parse();
                mObjects[i] = objParser.getParsedObject();
                mObjects[i].setPosition(mPosition[i][0], mPosition[i][1], mPosition[i][2]);
                materials[i] = new Material();
                materials[i].enableLighting(true);
                materials[i].setDiffuseMethod(new DiffuseMethod.Lambert());
                materials[i].setColor(colors[i]);
                mObjects[i].setMaterial(materials[i]);
                getCurrentScene().addChild(mObjects[i]);
            }

        } catch (ParsingException e) {
            e.printStackTrace();
        }

        getCurrentCamera().setPosition(0, 0, 0);
        getCurrentCamera().setRotation(0, 0, 0);

        // getCurrentCamera().enableLookAt();
        // getCurrentCamera().setLookAt(0, -20, 0);

        /****** show the detected ball in screen ******/
        /*
        OrthographicCamera orthoCam = new OrthographicCamera();
        orthoCam.setLookAt(0, 0, 0);
        orthoCam.enableLookAt();
        orthoCam.setY(0);
        orthoCam.setX(0);
        getCurrentScene().switchCamera(orthoCam);

        Material sphereMaterial = new Material();
        sphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        SpecularMethod.Phong phongMethod = new SpecularMethod.Phong();
        phongMethod.setShininess(180);
        sphereMaterial.setSpecularMethod(phongMethod);
        sphereMaterial.setAmbientIntensity(0, 0, 0);
        sphereMaterial.enableLighting(true);
        Sphere rootSphere = new Sphere(.2f, 12, 12);
        rootSphere.setMaterial(sphereMaterial);
        rootSphere.setRenderChildrenAsBatch(true);
        rootSphere.setVisible(false);
        getCurrentScene().addChild(rootSphere);

        int color = 0xFF0000;
        sphere = rootSphere.clone(false);
        sphere.setPosition(0, 0, 0);
        sphere.setMaterial(sphereMaterial);
        sphere.setColor(color);
        rootSphere.addChild(sphere);
        */

        /*
        for (int i = 0; i < 4; i++) {
            sphere_ary[i] = rootSphere.clone(false);
            sphere_ary[i].setPosition(0, 0, 0);
            sphere_ary[i].setMaterial(sphereMaterial);
            sphere_ary[i].setColor(color);
            rootSphere.addChild(sphere_ary[i]);
        }
        */
    }

    public void onTouchEvent(MotionEvent event){
    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j) {
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);

        data = ((MainActivity)context).getEulerAngleData();
        getCurrentCamera().setRotation(data.yaw, data.pitch, data.roll);


        // getCurrentCamera().setLookAt(0, -20, 0);

        /*
        long current_time = System.nanoTime();
        if (current_time - mTime > 5000000000L) {
            getCurrentCamera().setLookAt(mPosition[mLookAtIndex][0], mPosition[mLookAtIndex][1], mPosition[mLookAtIndex][2]);
            mTime = current_time;
            mLookAtIndex = (mLookAtIndex + 1) % 6;
        }
        */

        // getCurrentCamera().setRotation(0, 0, 0);

        // getCurrentCamera().setRotation(0, 0, 1, 1);
        // Quaternion q = new Quaternion();
        // q.setAll(1, 0, 0, 0);
        // getCurrentCamera().setOrientation(q);

        /*
        float[] angels = ((MainActivity)mContext).mSensorData.calculateAngles();
        float y = 10;
        float z, t;
        t = (float)(y / Math.tan(-angels[2]));
        z = 20 - t;
        getCurrentCamera().setLookAt(0, 0, z);
        */

        /****** show the detected ball in screen ******/
        /*
        HoughResultData data = ((MainActivity)context).getHoughResult();
        if (data == null || data.circleNumber == 0) {
            sphere.setVisible(false);
        } else {
            sphere.setVisible(true);
            float x_coord = pixel2coord(data.x - 1280 / 2);
            float y_coord = pixel2coord(data.y - 720 / 2);
            sphere.setScale(pixel2coord(data.r) / 0.2);
            sphere.setPosition(x_coord, -y_coord, 0);
        }
        */

        /*
        sphere_ary[0].setPosition(1.56f, 0.87f, 0);
        sphere_ary[1].setPosition(1.56f, -0.87f, 0);
        sphere_ary[2].setPosition(-1.56f, 0.87f, 0);
        sphere_ary[3].setPosition(-1.56f, -0.87f, 0);
        */
    }

    public static float pixel2coord(int value) {
        return 1.56097561f * 2 * value / 1280;
    }
}
