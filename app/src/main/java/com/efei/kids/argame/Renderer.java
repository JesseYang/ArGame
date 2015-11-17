package com.efei.kids.argame;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.OrthographicCamera;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;

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

    public void initScene(){

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

        int color = 0xfed14f;
        sphere = rootSphere.clone(false);
        sphere.setPosition(0, 0, 0);
        sphere.setMaterial(sphereMaterial);
        sphere.setColor(color);
        rootSphere.addChild(sphere);

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
        // earthSphere.rotate(Vector3.Axis.Y, 1.0);

        HoughResultData data = ((MainActivity)context).getHoughResult();

        float x, y, r;
        if (data == null || data.circleNumber == 0) {
            sphere.setVisible(false);
        } else {
            sphere.setVisible(true);
            float x_coord = pixel2coord(data.x - 1280 / 2);
            float y_coord = pixel2coord(data.y - 720 / 2);
            sphere.setScale(pixel2coord(data.r) / 0.2);
            sphere.setPosition(x_coord, -y_coord, 0);
        }

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
