package com.cgvsu.objwriter;

import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.ObjReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class ObjWriterTest {

    @Test
    public void testGetFileByPath01() throws IOException {
        String path = "";
        File file = ObjWriter.getObjFileByPath(path);
        boolean result = file.exists() && file.isFile();
        File checkingFile = new File("newFile.obj");
        boolean expectedResult = checkingFile.exists() && checkingFile.isFile();
        Assertions.assertEquals(result, expectedResult);
        file.delete();
    }


    @Test
    public void testGetFileByPath02() throws IOException {
        String path = "test";
        File file = ObjWriter.getObjFileByPath(path);
        boolean result = file.exists() && file.isFile();
        File checkingFile = new File("test.obj");
        boolean expectedResult = checkingFile.exists() && checkingFile.isFile();
        Assertions.assertEquals(result, expectedResult);
        file.delete();
    }

    @Test
    public void testGetFileByPath03() throws IOException {
        String path = "D:\\testFile.txt";
        File file = ObjWriter.getObjFileByPath(path);
        boolean result = file.exists() && file.isFile();
        File checkingFile = new File("D:\\testFile.obj");
        boolean expectedResult = checkingFile.exists() && checkingFile.isFile();
        Assertions.assertEquals(result, expectedResult);
        file.delete();
    }

    @Test
    public void testGetFileByPath04() throws IOException {
        String path = "D:\\";
        File file = ObjWriter.getObjFileByPath(path);
        boolean result = file.exists() && file.isFile();
        File checkingFile = new File("D:\\newFile.obj");
        boolean expectedResult = checkingFile.exists() && checkingFile.isFile();
        Assertions.assertEquals(result, expectedResult);
        file.delete();
    }

    @Test
    public void testCheckVerticesExistence01() {
        ArrayList<Vector3f> vertices = new ArrayList<>();
        try {
            ObjWriter.checkVerticesExistence(vertices);
        } catch (IllegalArgumentException exception) {
            String expectedError = "No vertices found";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testCheckVerticesExistence02() {
        ArrayList<Vector3f> vertices = null;
        try {
            ObjWriter.checkVerticesExistence(vertices);
        } catch (NullPointerException exception) {
            String expectedError = "List of vertices is null";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testGetStringVerticesContent01() {
        ArrayList<Vector3f> vertices = new ArrayList<>();
        final int COUNT = 5;
        final int DX = 1;
        final int DY = -1;
        for (int i = 0; i < COUNT; i++) {
            vertices.add(new Vector3f(i, i +DX, i +DY));
        }
        StringBuilder result = ObjWriter.getStringVerticesContent(vertices);
        StringBuilder expected = new StringBuilder();
        expected.append("v 0.0 1.0 -1.0\nv 1.0 2.0 0.0\nv 2.0 3.0 1.0\nv 3.0 4.0 2.0\nv 4.0 5.0 3.0\n");
        expected.append("# "+ COUNT+" vertices\n");
        Assertions.assertEquals(expected.toString(), result.toString());
        ;
    }

    @Test
    public void testGetStringTextureVerticesContent01() { //+
        ArrayList<Vector2f> textureVertices = new ArrayList<>();
        final int COUNT = 1000;
        final float DX = -23.875f;
        final float DY = 2.4354f;
        for (int i = 0; i < COUNT; i++) {
            textureVertices.add(new Vector2f((i + DX), (i + DY)));
        }
        StringBuilder result = ObjWriter.getStringTextureVerticesContent(textureVertices);
        String[] lines = result.toString().split("\n");
        String[] words;
        boolean flag = true;
        int i = 0;
        for (int j = 0; j < lines.length - 1; j++) {
            words = lines[j].split(" ");
            if (!words[0].equals("vt") || !(Float.parseFloat(words[1]) == i + DX)
                    || !(Float.parseFloat(words[2]) == i + DY)) {
                flag = false;
                break;
            }
            i++;
        }
        if (!lines[lines.length - 1].equals("# "+COUNT+ " texture coords")) {
            flag = false;
        }
        Assertions.assertTrue(flag);
    }

    @Test
    public void testGetStringTextureVerticesContent02() {
        ArrayList<Vector2f> textureVertices = new ArrayList<>();
        StringBuilder result = ObjWriter.getStringTextureVerticesContent(textureVertices);
        StringBuilder expected = new StringBuilder();
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetStringTextureVerticesContent03() {
        ArrayList<Vector2f> textureVertices = null;
        StringBuilder result = ObjWriter.getStringTextureVerticesContent(textureVertices);
        StringBuilder expected = new StringBuilder();
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetStringNormalsContent01() {//+
        ArrayList<Vector3f> normals = new ArrayList<>();
        final int COUNT = 526;
        final float DX = - 93.095f;
        final float DY = 14.0054f;
        for (int i = 0; i < COUNT; i++) {
            normals.add(new Vector3f((i +DX), (i + DY), i));
        }
        StringBuilder result = ObjWriter.getStringNormalsContent(normals);
        String[] lines = result.toString().split("\n");
        String[] words;
        boolean flag = true;
        int i = 0;
        for (int j = 0; j < lines.length - 1; j++){
            words = lines[j].split(" ");
            if (!words[0].equals("vn") || !(Float.parseFloat(words[1]) == i +DX)
                    || !(Float.parseFloat(words[2]) == i + DY) || !(Float.parseFloat(words[3]) == (float) i)) {
                flag = false;
                break;
            }
            i++;
        }
        if (!lines[lines.length - 1].equals("# "+COUNT+" vertex normals")) {
            flag = false;
        }
        Assertions.assertTrue(flag);
    }

    @Test
    public void testGetStringNormalsContent02() {
        ArrayList<Vector3f> normals = new ArrayList<>();
        StringBuilder result = ObjWriter.getStringNormalsContent(normals);
        StringBuilder expected = new StringBuilder();
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetStringNormalsContent03() {
        ArrayList<Vector3f> normals = null;
        StringBuilder result = ObjWriter.getStringNormalsContent(normals);
        StringBuilder expected = new StringBuilder();
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testPolygonInfoInObjForm01() {
        Polygon polygon = null;
        try {
            ObjWriter.polygonInfoInObjForm(polygon);
        } catch (NullPointerException exception) {
            String expectedError = "Polygon is null";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }

    }

    @Test
    public void testPolygonInfoInObjForm02() {
        Polygon polygon = new Polygon();
        try {
            ObjWriter.polygonInfoInObjForm(polygon);
        } catch (IllegalArgumentException exception) {
            String expectedError = "No vertices found in polygon";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }

    }

    @Test
    public void testPolygonInfoInObjForm03() {
        Polygon polygon = new Polygon();
        ArrayList<Integer> indexes = new ArrayList<>();
        final int COUNT = 50;
        for (int i = 0; i < COUNT; i++) {
            indexes.add(i);
        }
        polygon.setNormalIndices(indexes);
        polygon.setTextureVertexIndices(indexes);
        try {
            ObjWriter.polygonInfoInObjForm(polygon);
        } catch (IllegalArgumentException exception) {
            String expectedError = "No vertices found in polygon";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testPolygonInfoInObjForm04() { //+
        Polygon polygon = new Polygon();
        ArrayList<Integer> indexes = new ArrayList<>();
        final int vCount = 90;
        for (int i = 0; i < vCount; i++) {
            indexes.add(i);
        }
        polygon.setNormalIndices(indexes);
        polygon.setTextureVertexIndices(indexes);
        polygon.setVertexIndices(indexes);
        StringBuilder result = ObjWriter.polygonInfoInObjForm(polygon);
        String polygonInfo = result.toString();
        boolean flag = true;
        String[] words = polygonInfo.split(" ");
        String[] nums;
        int index = 1;
        for (int j = 0; j < words.length; j++) {
            if (!flag){
                break;
            }
            if (!words[j].matches("\\d+/\\d+/\\d+")) {
                flag = false;
                break;
            }
            nums = words[j].split("/");
            for (String num:nums){
                if (Integer.parseInt(num) != index){
                    flag = false;
                    break;
                }
            }
            index++;
        }

        Assertions.assertTrue(flag);
    }

    @Test
    public void testPolygonInfoInObjForm05() {//+
        Polygon polygon = new Polygon();
        ArrayList<Integer> indexes = new ArrayList<>();
        final int vCount = 903;
        for (int i = 0; i < vCount; i++) {
            indexes.add(i);
        }
        polygon.setNormalIndices(indexes);
        polygon.setVertexIndices(indexes);
        StringBuilder result = ObjWriter.polygonInfoInObjForm(polygon);
        String polygonInfo = result.toString();
        boolean flag = true;
        String[] words = polygonInfo.split(" ");
        String[] nums;
        int index = 1;
        for (int j = 0; j < words.length; j++) {
            if (!flag){
                break;
            }
            if (!words[j].matches("\\d+//\\d+")) {
                flag = false;
                break;
            }
            nums = words[j].split("//");
            for (String num:nums){
                if (Integer.parseInt(num) != index){
                    flag = false;
                    break;
                }
            }
            index++;
        }
        Assertions.assertTrue(flag);
    }

    @Test
    public void testPolygonInfoInObjForm06() {//-
        Polygon polygon = new Polygon();
        ArrayList<Integer> indexes = new ArrayList<>();
        final int vCount = 1010;
        for (int i = 0; i < vCount; i++) {
            indexes.add(i);
        }
        polygon.setTextureVertexIndices(indexes);
        polygon.setVertexIndices(indexes);
        StringBuilder result = ObjWriter.polygonInfoInObjForm(polygon);
        String polygonInfo = result.toString();
        boolean flag = true;
        String[] words = polygonInfo.split(" ");
        String[] nums;
        int index = 1;
        for (int j = 0; j < words.length; j++) {
            if (!flag){
                break;
            }
            if (!words[j].matches("\\d+/\\d+")) {
                flag = false;
                break;
            }
            nums = words[j].split("/");
            for (String num:nums){
                if (Integer.parseInt(num) != index){
                    flag = false;
                    break;
                }
            }
            index++;
        }
        Assertions.assertTrue(flag);
    }

    @Test
    public void testPolygonInfoInObjForm07() {//+
        Polygon polygon = new Polygon();
        ArrayList<Integer> indexes = new ArrayList<>();
        final int vCount = 10;
        for (int i = 0; i < vCount; i++) {
            indexes.add(i);
        }

        polygon.setVertexIndices(indexes);
        StringBuilder result = ObjWriter.polygonInfoInObjForm(polygon);
        String polygonInfo = result.toString();
        boolean flag = true;
        String[] words = polygonInfo.split(" ");
        int index = 1;
        for (int j = 0; j < words.length; j++) {
            if (Integer.parseInt(words[j]) != index) {
                flag = false;
                break;
            }
            index++;
        }
        Assertions.assertTrue(flag);
    }

    @Test
    void testGetStringPolygonsContent01() {
        ArrayList<Polygon> polygons = null;
        StringBuilder result = ObjWriter.getStringPolygonsContent(polygons);
        StringBuilder expected = new StringBuilder();
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    void testGetStringPolygonsContent02() {
        ArrayList<Polygon> polygons = new ArrayList<>();
        StringBuilder result = ObjWriter.getStringPolygonsContent(polygons);
        StringBuilder expected = new StringBuilder();
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    void testGetStringPolygonsContent03() {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon p1 = new Polygon();
        Polygon p2 = new Polygon();
        Polygon p3 = new Polygon();
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        ArrayList<Integer> arrayList3 = new ArrayList<>();
        final int COUNT = 4;
        final int DOP1 = 4;
        final int DOP2 = 1;
        for (int i = 1; i < COUNT; i++) {
            arrayList1.add(i);
            arrayList2.add(i + DOP2);
            arrayList3.add(i * DOP1);
        }
        p1.setVertexIndices(arrayList1);
        p2.setVertexIndices(arrayList2);
        p3.setVertexIndices(arrayList3);

        p1.setNormalIndices(arrayList2);
        p2.setNormalIndices(arrayList3);
        p3.setNormalIndices(arrayList1);

        p1.setTextureVertexIndices(arrayList3);
        p2.setTextureVertexIndices(arrayList1);
        p3.setTextureVertexIndices(arrayList2);

        polygons.add(p1);
        polygons.add(p2);
        polygons.add(p3);

        StringBuilder result = ObjWriter.getStringPolygonsContent(polygons);
        StringBuilder expected = new StringBuilder();
        expected.append("f 2/5/3 3/9/4 4/13/5 \nf 3/2/5 4/3/9 5/4/13 \nf 5/3/2 9/4/3 13/5/4 \n");
        expected.append("# " + polygons.size() + " polygons\n");
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    void testGetStringPolygonsContent04() {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon p1 = new Polygon();
        Polygon p2 = new Polygon();
        Polygon p3 = new Polygon();
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        ArrayList<Integer> arrayList3 = new ArrayList<>();
        final int COUNT = 4;
        final int DOP1 = 4;
        final int DOP2 = 1;
        for (int i = 1; i < COUNT; i++) {
            arrayList1.add(i);
            arrayList2.add(i + DOP2);
            arrayList3.add(i * DOP1);
        }
        p1.setVertexIndices(arrayList1);
        p2.setVertexIndices(arrayList2);
        p3.setVertexIndices(arrayList3);

        p1.setNormalIndices(arrayList2);
        p2.setNormalIndices(arrayList3);
        p3.setNormalIndices(arrayList1);


        polygons.add(p1);
        polygons.add(p2);
        polygons.add(p3);

        StringBuilder result = ObjWriter.getStringPolygonsContent(polygons);
        StringBuilder expected = new StringBuilder();
        expected.append("f 2//3 3//4 4//5 \nf 3//5 4//9 5//13 \nf 5//2 9//3 13//4 \n");
        expected.append("# " + polygons.size() + " polygons\n");
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    void testGetStringPolygonsContent05() {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon p1 = new Polygon();
        Polygon p2 = new Polygon();
        Polygon p3 = new Polygon();
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        ArrayList<Integer> arrayList3 = new ArrayList<>();
        final int COUNT = 4;
        final int DOP1 = 4;
        final int DOP2 = 1;
        for (int i = 1; i < COUNT; i++) {
            arrayList1.add(i);
            arrayList2.add(i + DOP2);
            arrayList3.add(i * DOP1);
        }
        p1.setVertexIndices(arrayList1);
        p2.setVertexIndices(arrayList2);
        p3.setVertexIndices(arrayList3);

        p1.setTextureVertexIndices(arrayList3);
        p2.setTextureVertexIndices(arrayList1);
        p3.setTextureVertexIndices(arrayList2);

        polygons.add(p1);
        polygons.add(p2);
        polygons.add(p3);

        StringBuilder result = ObjWriter.getStringPolygonsContent(polygons);
        StringBuilder expected = new StringBuilder();
        expected.append("f 2/5 3/9 4/13 \nf 3/2 4/3 5/4 \nf 5/3 9/4 13/5 \n");
        expected.append("# " + polygons.size() + " polygons\n");
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    void testGetStringPolygonsContent06() {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon p1 = new Polygon();
        Polygon p2 = new Polygon();
        Polygon p3 = new Polygon();
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        ArrayList<Integer> arrayList3 = new ArrayList<>();
        final int COUNT = 4;
        final int DOP1 = 4;
        final int DOP2 = 1;
        for (int i = 1; i < COUNT; i++) {
            arrayList1.add(i);
            arrayList2.add(i + DOP2);
            arrayList3.add(i * DOP1);
        }
        p1.setVertexIndices(arrayList1);
        p2.setVertexIndices(arrayList2);
        p3.setVertexIndices(arrayList3);

        polygons.add(p1);
        polygons.add(p2);
        polygons.add(p3);

        StringBuilder result = ObjWriter.getStringPolygonsContent(polygons);
        StringBuilder expected = new StringBuilder();
        expected.append("f 2 3 4 \nf 3 4 5 \nf 5 9 13 \n");
        expected.append("# " + polygons.size() + " polygons\n");
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    void testWrite01() throws IOException {
        Path fileName = Path.of("model1.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        ObjWriter.write(model, "result1.obj");
        Path fileNameAfter = Path.of("result1.obj");
        String fileContentAfter = Files.readString(fileNameAfter);

        String[] expectedStr = fileContent.split("\n");
        String[] resultStr = fileContentAfter.split("\n");
        int i = 0, j = 0;
        boolean flag = true;
        while (j < resultStr.length && i < expectedStr.length) {
            if (!expectedStr[i].startsWith("v") && !expectedStr[i].startsWith("vt") &&
                    !expectedStr[i].startsWith("vn") && !expectedStr[i].startsWith("f")) {
                i++;
                continue;
            }
            if (!resultStr[j].startsWith("v") && !resultStr[j].startsWith("vt") &&
                    !resultStr[j].startsWith("vn") && !resultStr[j].startsWith("f")) {
                j++;
                continue;
            }
            String[] wordsInExpectedStr = expectedStr[i].split("\\s+");
            String[] wordsInResultStr = resultStr[j].split("\\s+");
            if (wordsInExpectedStr.length != wordsInResultStr.length &&
                    (!wordsInExpectedStr[0].equals(wordsInResultStr[0]) && !wordsInExpectedStr[0].equals("vt"))) {
                flag = false;
                break;
            }
            if (wordsInExpectedStr[0].equals("vt")) {
                String[] arr = new String[wordsInExpectedStr.length - 1];
                for (int k = 0; k < arr.length; k++) {
                    arr[k] = wordsInExpectedStr[k];
                }
                wordsInExpectedStr = arr;
            }
            for (int k = 1; k < wordsInExpectedStr.length; k++) {
                if (wordsInExpectedStr[k].contains("/")) {
                    String[] ar1 = wordsInExpectedStr[k].split("/");
                    String[] ar2 = wordsInResultStr[k].split("/");
                    for (int t = 0; t < ar1.length; t++) {
                        assert Float.parseFloat(ar1[t]) - Float.parseFloat(ar2[t]) < Math.pow(0.1, 7);
                    }
                } else {
                    assert Float.parseFloat(wordsInExpectedStr[k]) - Float.parseFloat(wordsInResultStr[k]) < Math.pow(0.1, 10);
                }
            }
            i++;
            j++;
        }
        Assertions.assertTrue(flag);
    }
}