<template>
  <b-container fluid>
    <b-form-group id="input-groupfile" label="File:" label-for="fileInput">
      <b-form-file
        @change="handlefile"
        id="fileInput"
        type="file"
        name="fileInput"
        v-model="fileInput"
        placeholder="Choose a file or drop it here..."
        drop-placeholder="Drop file here..."
        required
      />
    </b-form-group>

    <b-form-group
      id="nameinput-groupfile"
      label="File Name:"
      label-for="filenNameInput"
    >
      <b-form-input
        id="filenNameInput"
        type="text"
        name="filenNameInput"
        v-model="filenNameInput"
        maxlength="100"
        placeholder="file name"
        required
      />
    </b-form-group>

    <b-form-group
      id="input-grouphashtag"
      label="Hashtag:"
      label-for="hashtagInput"
    >
      <b-form-input
        id="hashtagInput"
        type="text"
        name="hashtagInput"
        maxlength="100"
        v-model="hashtagInput"
        placeholder="Hashtags"
      />
    </b-form-group>
  </b-container>
</template>

<script>
export default {
  name: "File",
  props: ["file", "fileName", "hashtag", "extension"],
  data() {
    return {
      fileInput: null,
    };
  },
  methods: {
    //https://medium.com/js-dojo/how-to-upload-base64-images-in-vue-nodejs-4e89635daebc
    handlefile(event) {
      const selectedFile = event.target.files[0];
      this.transformbase64(selectedFile);
    },
    transformbase64(fileObject) {
      const reader = new FileReader(); //buffer where i receive the file
      //https://regex101.com/
      //exemple data url in base 64 data:text/plain;base64,SGVsbG8sIFdvcmxkIQ== 
      //^ start of a string; . any character; + one or more occourenncy
      reader.onload = (event) => {
        this.fileStringInput = event.target.result.replace( /^data:.+;base64,/,"");
      };
      reader.onerror = function (err) {
        console.log("Error: ", err);
      };
      reader.readAsDataURL(fileObject); //if reading done .result Dataurl and emission of the event onload gestito da reader.onlad

      //https://stackoverflow.com/questions/43708127/javascript-get-the-filename-and-extension-from-input-type-file
      let nameObject = fileObject.name;
      let lastDot = nameObject.lastIndexOf(".");
      this.$emit("update:extension", nameObject.substring(lastDot));
    },
  },
  watch: {
    fileStringInput: function () {
      if (this.fileStringInput === "") {
        this.fileInput = null;
      }
    },
  },
  computed: {
    fileStringInput: {
      get: function () {
        return this.file;
      },
      set: function (newValue) {
        this.$emit("update:file", newValue);
      },
    },
    filenNameInput: {
      get: function () {
        return this.fileName;
      },
      set: function (newValue) {
        this.$emit("update:fileName", newValue);
      },
    },
    hashtagInput: {
      get: function () {
        return this.hashtag;
      },
      set: function (newValue) {
        this.$emit("update:hashtag", newValue);
      },
    },
  },
};
</script>