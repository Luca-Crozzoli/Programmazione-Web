<template>
  <b-container fluid>
    <b-row>
      <b-col cols="6">
        <label for="dateFromIn">FROM</label>
        <b-form-datepicker
          id="dateFromIn"
          type="date"
          name="dateFromIn"
          v-model="dateFromIn"
          locale="en"
          menu-class="w-100"
          calendar-width="100%"
        ></b-form-datepicker>
      </b-col>

      <b-col cols="6">
        <label for="dateFromIn">TO</label>
        <b-form-datepicker
          id="dateToIn"
          type="date"
          name="dateToIn"
          v-model="dateToIn"
          locale="en"
          menu-class="w-100"
          calendar-width="100%"
        ></b-form-datepicker>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
export default {
  name: "Datepicker",
  props: ["dateFrom", "dateTo"],
  computed: {
    dateFromIn: {
      get: function () {
        return this.dateFrom;
      },
      set: function (newValue) {
        this.$emit("update:dateFrom", newValue);
      },
    },
    dateToIn: {
      get: function () {
        return this.dateTo;
      },
      set: function (newValue) {
        this.$emit("update:dateTo", newValue);
      },
    },
  },
};
</script>
