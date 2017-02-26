package com.badlogic.gdx.graphics.g3d.utils;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.Iterator;

public class ModelBuilder {
    private Array<MeshBuilder> builders;
    private Model model;
    private Node node;

    public ModelBuilder() {
        this.builders = new Array();
    }

    private MeshBuilder getBuilder(VertexAttributes attributes) {
        Iterator i$ = this.builders.iterator();
        while (i$.hasNext()) {
            MeshBuilder mb = (MeshBuilder) i$.next();
            if (mb.getAttributes().equals(attributes) && mb.lastIndex() < (short) 16383) {
                return mb;
            }
        }
        MeshBuilder result = new MeshBuilder();
        result.begin(attributes);
        this.builders.add(result);
        return result;
    }

    public void begin() {
        if (this.model != null) {
            throw new GdxRuntimeException("Call end() first");
        }
        this.node = null;
        this.model = new Model();
        this.builders.clear();
    }

    public Model end() {
        if (this.model == null) {
            throw new GdxRuntimeException("Call begin() first");
        }
        Model result = this.model;
        endnode();
        this.model = null;
        Iterator i$ = this.builders.iterator();
        while (i$.hasNext()) {
            ((MeshBuilder) i$.next()).end();
        }
        this.builders.clear();
        rebuildReferences(result);
        return result;
    }

    private void endnode() {
        if (this.node != null) {
            this.node = null;
        }
    }

    protected Node node(Node node) {
        if (this.model == null) {
            throw new GdxRuntimeException("Call begin() first");
        }
        endnode();
        this.model.nodes.add(node);
        this.node = node;
        return node;
    }

    public Node node() {
        Node node = new Node();
        node(node);
        node.id = "node" + this.model.nodes.size;
        return node;
    }

    public Node node(String id, Model model) {
        Node node = new Node();
        node.id = id;
        node.children.addAll(model.nodes);
        node(node);
        for (Disposable disposable : model.getManagedDisposables()) {
            manage(disposable);
        }
        return node;
    }

    public void manage(Disposable disposable) {
        if (this.model == null) {
            throw new GdxRuntimeException("Call begin() first");
        }
        this.model.manageDisposable(disposable);
    }

    public void part(MeshPart meshpart, Material material) {
        if (this.node == null) {
            node();
        }
        this.node.parts.add(new NodePart(meshpart, material));
    }

    public MeshPart part(String id, Mesh mesh, int primitiveType, int offset, int size, Material material) {
        MeshPart meshPart = new MeshPart();
        meshPart.id = id;
        meshPart.primitiveType = primitiveType;
        meshPart.mesh = mesh;
        meshPart.indexOffset = offset;
        meshPart.numVertices = size;
        part(meshPart, material);
        return meshPart;
    }

    public MeshPart part(String id, Mesh mesh, int primitiveType, Material material) {
        return part(id, mesh, primitiveType, 0, mesh.getNumIndices(), material);
    }

    private MeshPartBuilder part(String id, int primitiveType, VertexAttributes attributes, Material material) {
        MeshBuilder builder = getBuilder(attributes);
        part(builder.part(id, primitiveType), material);
        return builder;
    }

    public MeshPartBuilder part(String id, int primitiveType, long attributes, Material material) {
        return part(id, primitiveType, MeshBuilder.createAttributes(attributes), material);
    }

    public Model createBox(float width, float height, float depth, Material material, long attributes) {
        return createBox(width, height, depth, 4, material, attributes);
    }

    public Model createBox(float width, float height, float depth, int primitiveType, Material material, long attributes) {
        begin();
        part("box", primitiveType, attributes, material).box(width, height, depth);
        return end();
    }

    public Model createRect(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ, Material material, long attributes) {
        return createRect(x00, y00, z00, x10, y10, z10, x11, y11, z11, x01, y01, z01, normalX, normalY, normalZ, 4, material, attributes);
    }

    public Model createRect(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ, int primitiveType, Material material, long attributes) {
        begin();
        part("rect", primitiveType, attributes, material).rect(x00, y00, z00, x10, y10, z10, x11, y11, z11, x01, y01, z01, normalX, normalY, normalZ);
        return end();
    }

    public Model createCylinder(float width, float height, float depth, int divisions, Material material, long attributes) {
        return createCylinder(width, height, depth, divisions, 4, material, attributes);
    }

    public Model createCylinder(float width, float height, float depth, int divisions, int primitiveType, Material material, long attributes) {
        return createCylinder(width, height, depth, divisions, primitiveType, material, attributes, 0.0f, 360.0f);
    }

    public Model createCylinder(float width, float height, float depth, int divisions, Material material, long attributes, float angleFrom, float angleTo) {
        return createCylinder(width, height, depth, divisions, 4, material, attributes, angleFrom, angleTo);
    }

    public Model createCylinder(float width, float height, float depth, int divisions, int primitiveType, Material material, long attributes, float angleFrom, float angleTo) {
        begin();
        part("cylinder", primitiveType, attributes, material).cylinder(width, height, depth, divisions, angleFrom, angleTo);
        return end();
    }

    public Model createCone(float width, float height, float depth, int divisions, Material material, long attributes) {
        return createCone(width, height, depth, divisions, 4, material, attributes);
    }

    public Model createCone(float width, float height, float depth, int divisions, int primitiveType, Material material, long attributes) {
        return createCone(width, height, depth, divisions, primitiveType, material, attributes, 0.0f, 360.0f);
    }

    public Model createCone(float width, float height, float depth, int divisions, Material material, long attributes, float angleFrom, float angleTo) {
        return createCone(width, height, depth, divisions, 4, material, attributes, angleFrom, angleTo);
    }

    public Model createCone(float width, float height, float depth, int divisions, int primitiveType, Material material, long attributes, float angleFrom, float angleTo) {
        begin();
        part("cone", primitiveType, attributes, material).cone(width, height, depth, divisions, angleFrom, angleTo);
        return end();
    }

    public Model createSphere(float width, float height, float depth, int divisionsU, int divisionsV, Material material, long attributes) {
        return createSphere(width, height, depth, divisionsU, divisionsV, 4, material, attributes);
    }

    public Model createSphere(float width, float height, float depth, int divisionsU, int divisionsV, int primitiveType, Material material, long attributes) {
        return createSphere(width, height, depth, divisionsU, divisionsV, primitiveType, material, attributes, 0.0f, 360.0f, 0.0f, BitmapDescriptorFactory.HUE_CYAN);
    }

    public Model createSphere(float width, float height, float depth, int divisionsU, int divisionsV, Material material, long attributes, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {
        return createSphere(width, height, depth, divisionsU, divisionsV, 4, material, attributes, angleUFrom, angleUTo, angleVFrom, angleVTo);
    }

    public Model createSphere(float width, float height, float depth, int divisionsU, int divisionsV, int primitiveType, Material material, long attributes, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {
        begin();
        part("cylinder", primitiveType, attributes, material).sphere(width, height, depth, divisionsU, divisionsV, angleUFrom, angleUTo, angleVFrom, angleVTo);
        return end();
    }

    public Model createCapsule(float radius, float height, int divisions, Material material, long attributes) {
        return createCapsule(radius, height, divisions, 4, material, attributes);
    }

    public Model createCapsule(float radius, float height, int divisions, int primitiveType, Material material, long attributes) {
        begin();
        part("capsule", primitiveType, attributes, material).capsule(radius, height, divisions);
        return end();
    }

    public static void rebuildReferences(Model model) {
        model.materials.clear();
        model.meshes.clear();
        model.meshParts.clear();
        Iterator i$ = model.nodes.iterator();
        while (i$.hasNext()) {
            rebuildReferences(model, (Node) i$.next());
        }
    }

    private static void rebuildReferences(Model model, Node node) {
        Iterator i$ = node.parts.iterator();
        while (i$.hasNext()) {
            NodePart mpm = (NodePart) i$.next();
            if (!model.materials.contains(mpm.material, true)) {
                model.materials.add(mpm.material);
            }
            if (!model.meshParts.contains(mpm.meshPart, true)) {
                model.meshParts.add(mpm.meshPart);
                if (!model.meshes.contains(mpm.meshPart.mesh, true)) {
                    model.meshes.add(mpm.meshPart.mesh);
                }
                model.manageDisposable(mpm.meshPart.mesh);
            }
        }
        i$ = node.children.iterator();
        while (i$.hasNext()) {
            rebuildReferences(model, (Node) i$.next());
        }
    }

    @Deprecated
    public static Model createFromMesh(Mesh mesh, int primitiveType, Material material) {
        return createFromMesh(mesh, 0, mesh.getNumIndices(), primitiveType, material);
    }

    @Deprecated
    public static Model createFromMesh(Mesh mesh, int indexOffset, int vertexCount, int primitiveType, Material material) {
        Model result = new Model();
        MeshPart meshPart = new MeshPart();
        meshPart.id = "part1";
        meshPart.indexOffset = indexOffset;
        meshPart.numVertices = vertexCount;
        meshPart.primitiveType = primitiveType;
        meshPart.mesh = mesh;
        NodePart partMaterial = new NodePart();
        partMaterial.material = material;
        partMaterial.meshPart = meshPart;
        Node node = new Node();
        node.id = "node1";
        node.parts.add(partMaterial);
        result.meshes.add(mesh);
        result.materials.add(material);
        result.nodes.add(node);
        result.meshParts.add(meshPart);
        result.manageDisposable(mesh);
        return result;
    }

    @Deprecated
    public static Model createFromMesh(float[] vertices, VertexAttribute[] attributes, short[] indices, int primitiveType, Material material) {
        Mesh mesh = new Mesh(false, vertices.length, indices.length, attributes);
        mesh.setVertices(vertices);
        mesh.setIndices(indices);
        return createFromMesh(mesh, 0, indices.length, primitiveType, material);
    }
}
