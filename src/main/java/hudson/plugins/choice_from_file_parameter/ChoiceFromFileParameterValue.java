/*
 * The MIT License
 *
 * Copyright (c) 2012, Piotr Skotnicki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.choice_from_file_parameter;

import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.ParameterValue;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * {@link ParameterValue} created from {@link ChoiceFromFileParameterDefinition}.
 *
 * @author Piotr Skotnicki
 * @since 1.0
 */
public class ChoiceFromFileParameterValue extends ParameterValue {
    private String choice;

    @DataBoundConstructor
    public ChoiceFromFileParameterValue(String name, String choice) {
        super(name);
        this.choice = choice;
    }

    @Override
    public void buildEnvVars(AbstractBuild<?, ?> build, EnvVars env) {
        env.put(getName(), getChoice());
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    @Override
    public int hashCode() {
        final int prime = 71;
        int result = super.hashCode();
        result = prime * result;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (ChoiceFromFileParameterValue.class != obj.getClass()) {
            return false;
        }
        ChoiceFromFileParameterValue other = (ChoiceFromFileParameterValue)obj;
        if (choice == null) {
            if (other.choice != null) {
                return false;
            }
        } else if (!choice.equals(other.choice)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(ChoiceFromFileParameterValue) " + getName() + "='" + choice + "'";
    }
}
