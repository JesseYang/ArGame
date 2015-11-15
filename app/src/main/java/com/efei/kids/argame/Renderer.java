package com.efei.kids.argame;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
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
        setFrameRate(30);
    }

    public void initScene(){
        /*
        PointLight pointLight = new PointLight();
        pointLight.setY(2);
        pointLight.setPower(1.5f);

        getCurrentScene().addLight(pointLight);
        getCurrentCamera().setPosition(0, 2, 6);
        getCurrentCamera().setLookAt(0, 0, 0);

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

        // -- inner ring

        float radius = .8f;
        int count = 0;

        for (int i = 0; i < 360; i += 36) {
            double radians = MathUtil.degreesToRadians(i);
            int color = 0xfed14f;
            if (count % 3 == 0)
                color = 0x10a962;
            else if (count % 3 == 1)
                color = 0x4184fa;
            count++;

            Object3D sphere = rootSphere.clone(false);
            sphere.setPosition((float) Math.sin(radians) * radius, 0,
                    (float) Math.cos(radians) * radius);
            sphere.setMaterial(sphereMaterial);
            sphere.setColor(color);
            rootSphere.addChild(sphere);
        }

        // -- outer ring

        radius = 2.4f;
        count = 0;

        for (int i = 0; i < 360; i += 12) {
            double radians = MathUtil.degreesToRadians(i);
            int color = 0xfed14f;
            if (count % 3 == 0)
                color = 0x10a962;
            else if (count % 3 == 1)
                color = 0x4184fa;
            count++;

            Object3D sphere = rootSphere.clone(false);
            sphere.setPosition(Math.sin(radians) * radius, 0,
                    Math.cos(radians) * radius);
            sphere.setMaterial(sphereMaterial);
            sphere.setColor(color);
            rootSphere.addChild(sphere);
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
    }
}
