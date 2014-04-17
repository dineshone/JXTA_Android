/*
 * Copyright (c) 2001-2007 Sun Microsystems, Inc.  All rights reserved.
 *
 *  The Sun Project JXTA(TM) Software License
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *  3. The end-user documentation included with the redistribution, if any, must
 *     include the following acknowledgment: "This product includes software
 *     developed by Sun Microsystems, Inc. for JXTA(TM) technology."
 *     Alternately, this acknowledgment may appear in the software itself, if
 *     and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Sun", "Sun Microsystems, Inc.", "JXTA" and "Project JXTA" must
 *     not be used to endorse or promote products derived from this software
 *     without prior written permission. For written permission, please contact
 *     Project JXTA at http://www.jxta.org.
 *
 *  5. Products derived from this software may not be called "JXTA", nor may
 *     "JXTA" appear in their name, without prior written permission of Sun.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 *  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL SUN
 *  MICROSYSTEMS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  JXTA is a registered trademark of Sun Microsystems, Inc. in the United
 *  States and other countries.
 *
 *  Please see the license information page at :
 *  <http://www.jxta.org/project/www/license.html> for instructions on use of
 *  the license in source files.
 *
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many individuals
 *  on behalf of Project JXTA. For more information on Project JXTA, please see
 *  http://www.jxta.org.
 *
 *  This license is based on the BSD license adopted by the Apache Foundation.
 */
package net.jxta.impl.util.ternary;

import java.util.List;


public interface TernarySearchTree<E> {

    /**
     * Get the value E stored at the node referenced with <code>key</code>
     * If the node does not exist, it is created and the value <code>valueIfCreate</code> is stored
     *
     * @param key A string that indexes the object to be stored.
     * @param valueIfCreate The value to give to the node if is created
     * @return The value at the node
     */
    public E getOrCreate( final String key, final E valueIfCreate );

    /**
     * Stores value in the TernarySearchTree. The value may be retrieved using key.
     *
     * @param key A string that indexes the object to be stored.
     * @param value The object to be stored in the tree.
     */
    public abstract void put( final String key, final E value );

    /**
     * Retrieve the object indexed by key.
     *
     * @param key A String index.
     * @return Object The object retrieved from the TernarySearchTree.
     */
    public abstract E get( final String key );

    /**
     * Removes value indexed by key. Also removes all nodes that are rendered unnecessary by the removal of this data.
     *
     * @param key A string that indexes the object to be removed from the tree.
     */
    public abstract E remove( final String key );

    /**
     * Returns a list of ALL values in the tree that begin with prefix. Only keys for nodes having non-null data are included in the list.
     *
     * @param prefix Each key returned from this method will begin with the characters in prefix.
     * @return List of tree values that begin with the given prefix.
     */
    public abstract List<E> matchPrefix( final String prefix );

    /**
     * Returns a list of all values in the tree that begin with prefix. Only keys for nodes having non-null data are included in the list.
     *
     * @param prefix Each key returned from this method will begin with the characters in prefix.
     * @param listener A {@link TernarySearchTreeMatchListener} which will receive all data as it is found
     * and can stop the search at any time
     * @return List of tree values that begin with the given prefix.
     */
    public List<E> matchPrefix( final String prefix, final TernarySearchTreeMatchListener<E> listener );

    /**
     * Clears the tree and allows all heap space used by this tree to be garbage collected
     */
    public abstract void deleteTree(  );

    /**
     * Dumps the tree to stdout
     */
    public abstract void printTree(  );

    /**
     * Traverse the entire tree calling the listener for each data node found
     * Used for tree inspection
     */
    public abstract void walkTree( final TernarySearchTreeMatchListener<E> listener );
}
