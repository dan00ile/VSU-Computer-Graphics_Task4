package com.cgvsu.gui;

import com.cgvsu.math.vector.Vector3f;

public class Quaternion {
    public float x, y, z, w;

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }
    public Quaternion mul(Vector3f v) {
        // Преобразование вектора с использованием кватерниона
        Quaternion p = new Quaternion(v.getX(), v.getY(), v.getZ(), 0);
        Quaternion result = this.mul(p).mul(this.conjugate());

        return result;
    }


    public Quaternion mul(Quaternion q) {
        float newX = w * q.x + x * q.w + y * q.z - z * q.y;
        float newY = w * q.y - x * q.z + y * q.w + z * q.x;
        float newZ = w * q.z + x * q.y - y * q.x + z * q.w;
        float newW = w * q.w - x * q.x - y * q.y - z * q.z;

        return new Quaternion(newX, newY, newZ, newW);
    }
}
