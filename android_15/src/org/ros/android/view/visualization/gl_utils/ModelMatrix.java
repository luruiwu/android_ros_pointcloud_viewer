package org.ros.android.view.visualization.gl_utils;

import android.opengl.Matrix;
import org.ros.rosjava_geometry.Vector3;

/**
 * A class used for holding model data for opengl 3D objects.
 */
public class ModelMatrix {
	private float mModel[] = new float[16];

	public ModelMatrix() {
		Matrix.setIdentityM(mModel, 0);
	}
	public ModelMatrix(float m[]) {
		mModel = m;
	}

	public ModelMatrix clone(){
		ModelMatrix answer = new ModelMatrix();
		for(int i=0; i<mModel.length; i++){
			answer.mModel[i] = this.mModel[i];
		}
		return answer;
	}

	public ModelMatrix calTranslation(){
		ModelMatrix m = new ModelMatrix();
		m.translate(getX(), getY(), getZ());
		return m;
	}

	public void translate(float dx, float dy, float dz) {
		Matrix.translateM(this.mModel, 0, dx, dy, dz);
	}

	public void translate(Vector3 v) {
		Matrix.translateM(this.mModel, 0, (float) v.getX(), (float) v.getY(), (float) v.getZ());
	}

	//angle in degrees
	public void rotateX(float angle) {
		Matrix.rotateM(this.mModel, 0, angle, 1.0f, 0, 0);
	}

	//angle in degrees
	public void rotateY(float angle) {
		Matrix.rotateM(this.mModel, 0, angle, 0, 1.0f, 0);
	}

	//angle in degrees
	public void rotateZ(float angle) {
		Matrix.rotateM(this.mModel, 0, angle, 0, 0, 1.0f);
	}

	//angle in degrees
	public void rotate(float angle, float x, float y, float z) {
		Matrix.rotateM(this.mModel, 0, angle, x, y, z);
	}

	//Scales the object
	public void scale(float amount) {
		Matrix.scaleM(this.mModel, 0, amount, amount, amount);
	}

	/**
	 * @return The X position of this model.
	 */
	public float getX() {
		return mModel[12];
	}

	/**
	 * @return The Y position of this model.
	 */
	public float getY() {
		return mModel[13];
	}

	/**
	 * @return The Z position of this model.
	 */
	public float getZ() {
		return mModel[14];
	}

	/**
	 * @return The position of this model.
	 */
	public Vector3 getPosition() {
		return new Vector3(getX(), getY(), getZ());
	}

	/**
	 * @return A 3D point representing the X axis of this model.
	 */
	public Vector3 getAxisX() {
		return new Vector3(mModel[0], mModel[1], mModel[2]);
	}

	/**
	 * @return A 3D point representing the Y axis of this model.
	 */
	public Vector3 getAxisY() {
		return new Vector3(mModel[4], mModel[5], mModel[6]);
	}

	/**
	 * @return A 3D point representing the Z axis of this model.
	 */
	public Vector3 getAxisZ() {
		return new Vector3(mModel[8], mModel[9], mModel[10]);
	}

	/**
	 * @return A 3D point representing the X axis of this model.
	 * The axis is normalized.
	 */
	public Vector3 getAxisXNormalized() {
		return (new Vector3(mModel[0], mModel[1], mModel[2])).scale(1.f / getScaling());
	}

	/**
	 * @return A 3D point representing the Y axis of this model.
	 * The axis is normalized.
	 */
	public Vector3 getAxisYNormalized() {
		return (new Vector3(mModel[4], mModel[5], mModel[6])).scale(1.f / getScaling());
	}

	/**
	 * @return A 3D point representing the Z axis of this model.
	 * The axis is normalized.
	 */
	public Vector3 getAxisZNormalized() {
		return (new Vector3(mModel[8], mModel[9], mModel[10])).scale(1.f / getScaling());
	}

	/**
	 * Rotates around another's model axis.
	 * @param delta: Angle in degrees
	 * @param model: The model to rotate around.
	 */
	public void rotateOnModelX(float delta, ModelMatrix camera) {
		ModelMatrix translation = new ModelMatrix();
		translation.translate(this.getPosition());
		ModelMatrix invertedTranslation = translation.getInvertedMat();

		ModelMatrix appliedRotation = new ModelMatrix();
		appliedRotation.rotateX(delta);

		float answer[] = new float[16];
		Matrix.multiplyMM(answer, 0 , invertedTranslation.getMat(), 0 , this.mModel, 0);
		Matrix.multiplyMM(answer, 0 , appliedRotation.getMat(), 0 , answer, 0);
		Matrix.multiplyMM(answer, 0 , translation.getMat(), 0 , answer, 0);

		this.mModel = answer;
	}
	public float getScaling() {
		return (float) Math.sqrt(Math.pow(mModel[0], 2) + Math.pow(mModel[4], 2) + Math.pow(mModel[8], 2));
	}

	public final float[] getMat() {
		return mModel;
	}

	public ModelMatrix getInvertedMat(){
		ModelMatrix answer = new ModelMatrix();
		Matrix.invertM(answer.mModel, 0 , this.mModel, 0);
		return answer;
	}

	/**
	 * Sets this model to the identity matrix ("resets" the model).
	 */
	public void setIdentity() {
		Matrix.setIdentityM(mModel, 0);
	}


	public ModelMatrix mult(ModelMatrix m){
		float a[] = new float[16];
		Matrix.multiplyMM(a, 0, mModel,0, m.mModel,0);
		return new ModelMatrix(a);
	}

}
