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

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;

import java.util.regex.Pattern;

import java.io.*;

/**
 * Choice based parameter that loads entries from text file.
 * 
 * @author Piotr Skotnicki
 * @since 1.0
 * @see {@link ParameterDefinition}
 */
public class ChoiceFromFileParameterDefinition extends ParameterDefinition {

    private static final long serialVersionUID = 1L;
    private String choiceFilePath;
    private String sorting;
    private String filtering;

    @DataBoundConstructor
    public ChoiceFromFileParameterDefinition(String name, String choiceFilePath, String sorting, String filtering, String description) {
        super(name, description);
        this.choiceFilePath = choiceFilePath;
        this.sorting = sorting;
        this.filtering = filtering;
    }

    public ChoiceFromFileParameterDefinition(String name, String choiceFilePath, String sorting, String filtering) {
        this(name, choiceFilePath, sorting, filtering, null);
    }

    public String getChoiceFilePath() {
        return choiceFilePath;
    }

    public String getSorting() {
        return sorting;
    }

    public String getFiltering() {
        return filtering;
    }

    public String getRootUrl() {
        return Hudson.getInstance().getRootUrl();
    }
    
    @Override
    public ChoiceFromFileParameterValue getDefaultParameterValue() {
        ChoiceFromFileParameterValue v = new ChoiceFromFileParameterValue(getName(), "(null)");
        return v;
    }

    public List<String> getChoices() {
        List<String> choices = new ArrayList<String>();

        try
        {
            FileInputStream fstream = new FileInputStream(this.choiceFilePath);

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                if(strLine.trim().isEmpty())
                    continue;

                if (filtering != null && !filtering.trim().isEmpty() && !Pattern.matches(filtering, strLine))
                    continue;

                choices.add(strLine);
            }

            in.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        // sort elements
        if (sorting != null) {
            if (sorting.equals("ascending")) {
                Collections.sort(choices, String.CASE_INSENSITIVE_ORDER);
            } else if (sorting.equals("descending")) {
                Collections.sort(choices, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
            }
        }

        // remove duplicates
        choices = new ArrayList(new LinkedHashSet<String>(choices));

        if (choices.isEmpty()) {
            choices.add("(null)");
        }

        return choices;
	}

    @Extension
    public static class DescriptorImpl extends ParameterDescriptor {
        @Override
        public String getDisplayName() {
            return "Choice From File Parameter";
        }

        @Override
        public String getHelpFile() {
            return "/plugin/choice-from-file-parameter/help.html";
        }
    }

    @Override
    public ParameterValue createValue(StaplerRequest req, JSONObject jo) {
        ChoiceFromFileParameterValue value = req.bindJSON(ChoiceFromFileParameterValue.class, jo);
        value.setDescription(getDescription());
        return value;
    }

    @Override
    public ParameterValue createValue(StaplerRequest req) {
        String[] value = req.getParameterValues(getName());
        if (value == null || value.length < 1) {
            return getDefaultParameterValue();
        } else {
            return new ChoiceFromFileParameterValue(getName(), value[0]);
        }
    }
}
