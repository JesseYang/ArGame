package com.efei.kids.argame;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.OrthographicCamera;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;

/**
 * Created by jesse on 15-11-12.
 */
public class Renderer extends RajawaliRenderer {


    public Context context;
    private DirectionalLight directionalLight;
    private Sphere earthSphere;
    private Object3D mObjectGroup;

    public Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60.0);
    }

    public void initScene(){

        HoughResultData data = ((MainActivity)context).getHoughResult();

        float x, y, r;
        if (data == null || data.circleNumber == 0) {
            x = 0.0f;
            y = 0.0f;
            r = 0.4f;
        } else {
            x = 0.0f;
            y = 0.0f;
            r = 0.2f;
        }

        OrthographicCamera orthoCam = new OrthographicCamera();
        orthoCam.setLookAt(0, 0, 0);
        orthoCam.enableLookAt();
        orthoCam.setY(0);
        orthoCam.setX(0);
        getCurrentScene().switchCamera(orthoCam);

        final DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setPosition(0.0, 0.0, 1.0);
        directionalLight.setPower(1.5f);
        directionalLight.setLookAt(Vector3.ZERO);
        directionalLight.enableLookAt();
        getCurrentScene().addLight(directionalLight);

        Material sphereMaterial = new Material();
        sphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        SpecularMethod.Phong phongMethod = new SpecularMethod.Phong();
        phongMethod.setShininess(180);
        sphereMaterial.setSpecularMethod(phongMethod);
        sphereMaterial.setAmbientIntensity(0, 0, 0);
        sphereMaterial.enableLighting(true);
        Sphere rootSphere = new Sphere(r, 12, 12);
        rootSphere.setMaterial(sphereMaterial);
        rootSphere.setRenderChildrenAsBatch(true);
        rootSphere.setVisible(false);
        getCurrentScene().addChild(rootSphere);

        int color = 0xfed14f;
        Object3D sphere = rootSphere.clone(false);
        sphere.setPosition(x, y, 0);
        sphere.setMaterial(sphereMaterial);
        sphere.setColor(color);
        rootSphere.addChild(sphere);
    }

    public void onTouchEvent(MotionEvent event){
    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j) {
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        // earthSphere.rotate(Vector3.Axis.Y, 1.0);
    }
}
