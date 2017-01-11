package com.bolin.graphics;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.bolin.math.vector.PolarVector3D;
import com.bolin.math.vector.SimpleVector;
import com.bolin.math.vector.Vector3D;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jonb3_000
 */
public class JoglCanvas extends GLCanvas implements GLEventListener {

    public static final int FPS = 60;

    private FPSAnimator animator;
    private GLU glu;
    private ArrayList<Drawable> drawables;
    private Vector3D viewPoint;
    private PolarVector3D viewDirection;

    public JoglCanvas() {
        super();
        addGLEventListener(this);
        drawables = new ArrayList<>();
        viewPoint = new Vector3D(0, 0, 0);
        viewDirection = new PolarVector3D(1, -Math.PI / 2, Math.PI / 2, true);
        System.err.println(viewDirection);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glClearColor(0f, 0f, 0f, 1f);

        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = {1000, 0, 100, SHINE_ALL_DIRECTIONS};
        float[] lightColorAmbient = {1f, 1f, 1f, 1f};
        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};

        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);

        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glEnable(GL2.GL_LIGHT1);
        gl.glEnable(GL2.GL_LIGHTING);

        float[] rgba = {0.3f, 0.5f, 1f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.5f);

        animator = new FPSAnimator(this, FPS);
        animator.start();

        glu = new GLU();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        setCamera(gl, glu);

        if (drawables != null) {
            for (int i = 0; i < drawables.size(); i++) {
                Drawable s = drawables.get(i);
                if (s instanceof SphereInterface && s.isReady()) {
                    drawSphere(gl, (SphereInterface) s);
                } else if (s instanceof PolygonInterface && s.isReady()) {
                    drawPolygon(gl, (PolygonInterface) s);
                }
            }
        }
    }

    public void setDrawables(ArrayList<Drawable> s) {
        drawables = s;
    }

    public void drawPolygon(GL2 gl, PolygonInterface p) {
        float[][] vertices = p.getVertices();
        gl.glBegin(GL2.GL_POLYGON);
        gl.glColor3f(p.getColor()[0], p.getColor()[1], p.getColor()[2]);
        for (float[] f : vertices) {
            gl.glVertex3f(f[0], f[1], f[2]);
        }
//        gl.glVertex3f(vertices[0][0], vertices[0][1], vertices[0][2]);
        gl.glEnd();
    }

    public void drawSphere(GL2 gl, SphereInterface s) {
        double[] values = s.getLocation().getValues();
        gl.glTranslatef((float) values[0], (float) values[1], (float) values[2]);

        if (s.hasTexture()) {
            System.out.println(s.getTextureLocation());
            if (s.getTexture() == null) {
                try {
                    InputStream stream = new FileInputStream(s.getTextureLocation());
                    TextureData data;
                    data = TextureIO.newTextureData(gl.getGLProfile(), stream, true, "jpg");
                    s.setTexture(TextureIO.newTexture(data));
                } catch (IOException ex) {
                    Logger.getLogger(JoglCanvas.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            s.getTexture().enable(gl);
            s.getTexture().bind(gl);
        }

        gl.glColor3f(s.getColor()[0], s.getColor()[1], s.getColor()[2]);
        GLUquadric sphere = glu.gluNewQuadric();
        glu.gluQuadricTexture(sphere, true);
        glu.gluQuadricDrawStyle(sphere, GLU.GLU_FILL);
        glu.gluQuadricNormals(sphere, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(sphere, GLU.GLU_OUTSIDE);
        final float radius = (float) s.getRadius();
        final int slices = 16;
        final int stacks = 16;
        glu.gluSphere(sphere, radius, slices, stacks);
        glu.gluDeleteQuadric(sphere);
        gl.glTranslatef((float) -values[0], (float) -values[1], (float) -values[2]);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    private void setCamera(GL2 gl, GLU glu) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float widthHeightRatio = (float) getWidth() / (float) getHeight();
        glu.gluPerspective(45, widthHeightRatio, 1, 1E20);
        glu.gluLookAt(0, 0, 0, viewDirection.x(), viewDirection.z(), viewDirection.y(), 0, 1, 0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glTranslatef((float) viewPoint.x(), (float) viewPoint.y(), (float) viewPoint.z());
    }

    public void addDrawable(Drawable d) {
        this.drawables.add(d);
    }

    public void changeViewpoint(SimpleVector amount) {
        viewPoint.add(amount);
    }

    public void changeViewDirection(double theta, double phi) {
        if (viewDirection.phi() + phi > Math.PI - Math.PI / 100 || viewDirection.phi() + phi < Math.PI / 100) {
            phi = 0;
        }
        viewDirection.rotate(theta, phi);
    }

    public void setViewpoint(Vector3D v) {
        viewPoint = v;
    }

    public Vector3D getView() {
        return viewPoint;
    }

    public PolarVector3D getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(PolarVector3D v) {
        viewDirection = v;
    }

}
