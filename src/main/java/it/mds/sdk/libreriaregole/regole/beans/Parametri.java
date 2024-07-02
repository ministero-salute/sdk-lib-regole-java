/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.libreriaregole.regole.beans;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class Parametri {
    private Map<String,String> parametriMap;

    public Map<String, String> getParametriMap() {
        if(parametriMap == null){
            parametriMap = new HashMap<>();
        }
        return parametriMap ;
    }
}
