// RuntimeTypeAdapterFactory.java
// Adapter for polymorphic serialization with Gson
// Source: Google Gson Extras (Apache 2.0 License)

package database;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.*;
import java.io.*;
import java.util.*;

/**
 * RuntimeTypeAdapterFactory allows Gson to serialize/deserialize polymorphic types.
 * This is needed for abstract class Soggetto and its subclasses.
 */
public final class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {
    
    private final Class<?> baseType;
    private final String typeFieldName;
    private final Map<String, Class<?>> labelToSubtype = new LinkedHashMap<>();
    private final Map<Class<?>, String> subtypeToLabel = new LinkedHashMap<>();

    private RuntimeTypeAdapterFactory(Class<?> baseType, String typeFieldName) {
        if (typeFieldName == null || baseType == null) {
            throw new NullPointerException();
        }
        this.baseType = baseType;
        this.typeFieldName = typeFieldName;
    }

    /**
     * Creates a new runtime type adapter for the specified base type.
     */
    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName) {
        return new RuntimeTypeAdapterFactory<>(baseType, typeFieldName);
    }

    /**
     * Creates a new runtime type adapter using "type" as the type field name.
     */
    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType) {
        return new RuntimeTypeAdapterFactory<>(baseType, "type");
    }

    /**
     * Registers a subtype with the specified label.
     */
    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> type, String label) {
        if (type == null || label == null) {
            throw new NullPointerException();
        }
        if (subtypeToLabel.containsKey(type) || labelToSubtype.containsKey(label)) {
            throw new IllegalArgumentException("Types and labels must be unique");
        }
        labelToSubtype.put(label, type);
        subtypeToLabel.put(type, label);
        return this;
    }

    /**
     * Registers a subtype using the class simple name as label.
     */
    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> type) {
        return registerSubtype(type, type.getSimpleName());
    }

    @Override
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
        if (type.getRawType() != baseType) {
            return null;
        }

        final Map<String, TypeAdapter<?>> labelToDelegate = new LinkedHashMap<>();
        final Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = new LinkedHashMap<>();

        for (Map.Entry<String, Class<?>> entry : labelToSubtype.entrySet()) {
            TypeAdapter<?> delegate = gson.getDelegateAdapter(this, TypeToken.get(entry.getValue()));
            labelToDelegate.put(entry.getKey(), delegate);
            subtypeToDelegate.put(entry.getValue(), delegate);
        }

        return new TypeAdapter<R>() {
            @Override
            public R read(JsonReader in) throws IOException {
                JsonElement jsonElement = JsonParser.parseReader(in);
                JsonElement labelJsonElement = jsonElement.getAsJsonObject().get(typeFieldName);
                
                if (labelJsonElement == null) {
                    throw new JsonParseException("Cannot deserialize " + baseType
                            + " because it does not have a field named " + typeFieldName);
                }
                
                String label = labelJsonElement.getAsString();
                @SuppressWarnings("unchecked")
                TypeAdapter<R> delegate = (TypeAdapter<R>) labelToDelegate.get(label);
                
                if (delegate == null) {
                    throw new JsonParseException("Cannot deserialize " + baseType + " subtype named "
                            + label + "; registered subtypes: " + labelToSubtype.keySet());
                }
                
                return delegate.fromJsonTree(jsonElement);
            }

            @Override
            public void write(JsonWriter out, R value) throws IOException {
                Class<?> srcType = value.getClass();
                String label = subtypeToLabel.get(srcType);
                
                @SuppressWarnings("unchecked")
                TypeAdapter<R> delegate = (TypeAdapter<R>) subtypeToDelegate.get(srcType);
                
                if (delegate == null) {
                    throw new JsonParseException("Cannot serialize " + srcType.getName()
                            + "; registered subtypes: " + subtypeToLabel.keySet());
                }

                JsonObject jsonObject = delegate.toJsonTree(value).getAsJsonObject();
                
                JsonObject clone = new JsonObject();
                clone.addProperty(typeFieldName, label);
                
                for (Map.Entry<String, JsonElement> e : jsonObject.entrySet()) {
                    clone.add(e.getKey(), e.getValue());
                }
                
                Streams.write(clone, out);
            }
        }.nullSafe();
    }
}
