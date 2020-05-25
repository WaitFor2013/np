/**
 * Copyright 2009-2015 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.np.database.domain;

import com.np.database.orm.TablePrefix;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Table(name = "author")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Author implements Serializable {

    @Id
    @Column(name = "id")
    @TablePrefix("a")
    protected Integer id;

    @Column(name = "username")
    @TablePrefix("a")
    protected String username;

    @Column(name = "password")
    @TablePrefix("a")
    protected String password;

    @Column(name = "email")
    @TablePrefix("a")
    protected String email;

    @Column(name = "bio")
    @TablePrefix("a")
    protected String bio;

    @Column(name = "favourite_section")
    @TablePrefix("a")
    protected Section favouriteSection;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;

        Author author = (Author) o;

        if (id != author.id) return false;
        if (bio != null ? !bio.equals(author.bio) : author.bio != null) return false;
        if (email != null ? !email.equals(author.email) : author.email != null) return false;
        if (password != null ? !password.equals(author.password) : author.password != null) return false;
        if (username != null ? !username.equals(author.username) : author.username != null) return false;
        if (favouriteSection != null ? !favouriteSection.equals(author.favouriteSection) : author.favouriteSection != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (bio != null ? bio.hashCode() : 0);
        result = 31 * result + (favouriteSection != null ? favouriteSection.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Author : " + id + " : " + username + " : " + email;
    }
}