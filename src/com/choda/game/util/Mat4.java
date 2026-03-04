package com.choda.game.util;

public class Mat4 {
    double[][] m;
    {
        m = new double[4][4];
    }
    public Mat4() {

    }
    public Mat4(double[][] m) throws IllegalArgumentException, NullPointerException {
        if (m.length != 4) {
            throw new IllegalArgumentException();
        } else {
            for (double[] row : m) {
                if (row.length != 4) {
                    throw new IllegalArgumentException();
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.m[i][j] = m[i][j];
            }
        }
    }
    public Mat4(Mat4 mat4) throws NullPointerException {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.m[i][j] = mat4.m[i][j];
            }
        }
    }
    public Mat4 add(Mat4 mat4) {
        Mat4 result = new Mat4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.m[i][j] = this.m[i][j] + mat4.m[i][j];
            }
        }
        return result;
    }
    public Mat4 sub(Mat4 mat4) {
        Mat4 result = new Mat4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.m[i][j] = this.m[i][j] - mat4.m[i][j];
            }
        }
        return result;
    }
    public Mat4 dot(double scalar) {
        Mat4 result = new Mat4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.m[i][j] = scalar * this.m[i][j];
            }
        }
        return result;
    }
    public Mat4 dot(Mat4 mat4) {
        Mat4 result = new Mat4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    result.m[i][j] += this.m[i][k] * mat4.m[k][j];
                }
            }
        }
        return result;
    }
    public Vec4 dot(Vec4 v4) {
        return new Vec4(this.m[0][0] * v4.x + this.m[0][1] * v4.y + this.m[0][2] * v4.z + this.m[0][3] * v4.w,
                this.m[1][0] * v4.x + this.m[1][1] * v4.y + this.m[1][2] * v4.z + this.m[1][3] * v4.w,
                this.m[2][0] * v4.x + this.m[2][1] * v4.y + this.m[2][2] * v4.z + this.m[2][3] * v4.w,
                this.m[3][0] * v4.x + this.m[3][1] * v4.y + this.m[3][2] * v4.z + this.m[3][3] * v4.w);
    }
    public double[][] toArray() {
        double[][] result = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = this.m[i][j];
            }
        }
        return result;
    }
    public static Mat4 makeIdentity() {
        Mat4 mat4 = new Mat4();
        mat4.m[0][0] = 1.0;
        mat4.m[1][1] = 1.0;
        mat4.m[2][2] = 1.0;
        mat4.m[3][3] = 1.0;
        return mat4;
    }
    public static Mat4 makeTranslation(Vec3 velocity) {
        Mat4 mat4 = makeIdentity();
        mat4.m[0][3] = velocity.x;
        mat4.m[1][3] = velocity.y;
        mat4.m[2][3] = velocity.z;
        return mat4;
    }
    public static Mat4 makeRotation(double radian, Vec3 axis) {
        Mat4 mat4 = makeIdentity();
        double cos = Math.cos(radian);
        double sin = Math.sin(radian);
        axis = axis.normalize();
        double x = axis.x, y = axis.y, z = axis.z;
        mat4.m[0][0] = cos + x * x * (1 - cos);
        mat4.m[0][1] = x * y * (1 - cos) - z * sin;
        mat4.m[0][2] = x * z * (1 - cos) + y * sin;
        mat4.m[0][3] = 0;
        mat4.m[1][0] = y * x * (1 - cos) + z * sin;
        mat4.m[1][1] = cos + y * y * (1 - cos);
        mat4.m[1][2] = y * z * (1 - cos) + x * sin;
        mat4.m[1][3] = 0;
        mat4.m[2][0] = z * x * (1 - cos) - y * sin;
        mat4.m[2][1] = z * y * (1 - cos) - x * sin;
        mat4.m[2][2] = cos + z * z * (1 - cos);
        mat4.m[2][3] = 0;
        mat4.m[3][0] = 0;
        mat4.m[3][1] = 0;
        mat4.m[3][2] = 0;
        mat4.m[3][3] = 1;
        return mat4;
    }
    public static Mat4 makeScalar(double scalar) {
        Mat4 mat4 = makeIdentity();
        mat4.m[0][0] *= scalar;
        mat4.m[1][1] *= scalar;
        mat4.m[2][2] *= scalar;
        return mat4;
    }
    public static Mat4 makeFrustumY(double fovY, double aspectRatio, double front, double back) {
        Mat4 mat4 = new Mat4();
        double tan = Math.tan(fovY/2);
        double top = front * tan;
        double right = top / aspectRatio;

        mat4.m[0][0] = front / right;
        mat4.m[1][1] = front / top;
        mat4.m[2][2] = -(back + front) / (back - front);
        mat4.m[2][3] = -(2 * back * front) / (back - front);
        mat4.m[3][2] = -1;
        mat4.m[3][3] = 0;
        return mat4;
    }
    public static Mat4 makeViewport(int x, int y, int width, int height) {
        Mat4 mat4 = new Mat4();
        mat4.m[0][0] = width / 2.0;
        mat4.m[0][3] = x + width / 2.0;
        mat4.m[1][1] = -height / 2.0;
        mat4.m[1][3] = y + height / 2.0;
        mat4.m[2][2] = (1.0 - 0.0) / 2.0;
        mat4.m[2][3] = (1.0 + 0.0) / 2.0;
        mat4.m[3][3] = 1.0;
        return mat4;
    }
    public static Mat4 makeLookAt(Vec3 position, Vec3 target, Vec3 up) {
        Mat4 leftMat4 = makeIdentity(), rightMat4 = makeIdentity();
        Vec3 direction = position.sub(target).normalize();
        Vec3 right = up.cross(direction).normalize();
        Vec3 cameraUp = direction.cross(right).normalize();
        leftMat4.m[0][0] = right.x;
        leftMat4.m[0][1] = right.y;
        leftMat4.m[0][2] = right.z;
        leftMat4.m[1][0] = cameraUp.x;
        leftMat4.m[1][1] = cameraUp.y;
        leftMat4.m[1][2] = cameraUp.z;
        leftMat4.m[2][0] = direction.x;
        leftMat4.m[2][1] = direction.y;
        leftMat4.m[2][2] = direction.z;

        rightMat4.m[0][3] = -position.x;
        rightMat4.m[1][3] = -position.y;
        rightMat4.m[2][3] = -position.z;
        return leftMat4.dot(rightMat4);
    }
}
