/**
 * Copyright (c) 2016 Denis O <denis@universal-development.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unidev.polydata.insights.model;


import com.unidev.polydata.insights.Application;
import org.springframework.hateoas.ResourceSupport;

/**
 * Generic hataeos response object
 * @param <T>
 */
public class HateoasResponse<T> extends ResourceSupport {

    String version = Application.VERSION;
    T data;

    public static HateoasResponse hateoasResponse() {
        return new HateoasResponse();
    }

    public static <T> HateoasResponse hateoasResponse(T data) {
        HateoasResponse hateoasResponse = new HateoasResponse();
        hateoasResponse.setData(data);
        return hateoasResponse;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public HateoasResponse data(T data) {
        this.data = data;
        return this;
    }
}
