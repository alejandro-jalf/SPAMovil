package com.example.spamovil.models;

import com.example.spamovil.Services.ConexionSqlServer;
import static com.example.spamovil.Services.Instances.getConexionSqlServer;
import static com.example.spamovil.Services.Instances.getConfigs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Articulos {
    private ConexionSqlServer conexionSqlServer;
    private Context context;
    private Connection connection;

    public Articulos(Context context) {
        this.conexionSqlServer = new ConexionSqlServer(context, getConfigs());
        this.context = context;
    }

    public JSONObject getPrecioArticulo(String sucursal, String barcode) {
        JSONObject result = null;
        try {
            connection = conexionSqlServer.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("" +
                    "DECLARE @Sucursal NVARCHAR(2) = '" + sucursal + "' \n" +
                    "DECLARE @Almacen INT = CASE WHEN @Sucursal = 'ZR' THEN 2 WHEN @Sucursal = 'VC' THEN 3 WHEN @Sucursal = 'OU' THEN 19 WHEN @Sucursal = 'JL' THEN 7 WHEN @Sucursal = 'BO' THEN 21 ELSE 0 END; \n" +
                    "DECLARE @Tienda INT = CASE WHEN @Sucursal = 'ZR' THEN 1 WHEN @Sucursal = 'VC' THEN 2 WHEN @Sucursal = 'OU' THEN 5 WHEN @Sucursal = 'JL' THEN 4 WHEN @Sucursal = 'BO' THEN 6 ELSE 0 END; \n" +
                    "DECLARE @articulo NVARCHAR(15) = (SELECT DISTINCT TOP 1 Articulo FROM ArticulosRelacion WHERE CodigoBarras = '" + barcode + "' OR Articulo = '" + barcode + "');" +
                    "DECLARE @article NVARCHAR(15) = ISNULL(@articulo, '" + barcode + "'); \n" +
                    "SELECT \n" +
                    "    A.Articulo, \n" +
                    "    A.CodigoBarras, \n" +
                    "    A.Nombre, \n" +
                    "    A.Descripcion, \n" +
                    "    A.Precio1IVAUV, \n" +
                    "    A.CantidadParaPrecio1, \n" +
                    "    A.Precio2IVAUV, \n" +
                    "    A.CantidadParaPrecio2, \n" +
                    "    A.Precio3IVAUV, \n" +
                    "    A.CantidadParaPrecio3 \n" +
                    "FROM \n" +
                    "    QVListaprecioConCosto AS A \n" +
                    "INNER JOIN QVExistencias AS E ON A.Articulo = E.Articulo \n" +
                    "WHERE (A.Articulo = @article OR A.CodigoBarras = @article) \n" +
                    "    AND A.Tienda = @Tienda \n" +
                    "    AND A.Almacen = @Almacen \n" +
                    "    AND E.Tienda = @Tienda \n" +
                    "    AND E.Almacen = @Almacen \n");

            result = new JSONObject();
            while (resultSet.next()) {
                result.put("Articulo", resultSet.getString("Articulo"));
                result.put("CodigoBarras", resultSet.getString("CodigoBarras"));
                result.put("Nombre", resultSet.getString("Nombre"));
                result.put("Descripcion", resultSet.getString("Descripcion"));
                result.put("Precio1IVAUV", resultSet.getString("Precio1IVAUV"));
                result.put("CantidadParaPrecio1", resultSet.getString("CantidadParaPrecio1"));
                result.put("Precio2IVAUV", resultSet.getString("Precio2IVAUV"));
                result.put("CantidadParaPrecio2", resultSet.getString("CantidadParaPrecio2"));
                result.put("Precio3IVAUV", resultSet.getString("Precio3IVAUV"));
                result.put("CantidadParaPrecio3", resultSet.getString("CantidadParaPrecio3"));
            }
        } catch (JSONException je) {
            Toast.makeText(context, je.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (SQLException se) {
            Toast.makeText(context, se.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }
}
