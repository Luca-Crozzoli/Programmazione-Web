<template>
  <div>
    <b-toast
      id="error_toast"
      title="ERR"
      variant="danger"
      >{{ error }}</b-toast
    >
    <b-toast
      id="warning_toast"
      title="WAR"
      variant="warning"
      >{{ warning }}</b-toast
    >
    <b-toast
      id="success_toast"
      title="SUCCESS"
      variant="success"
      >{{ success }}</b-toast
    >
  </div>
</template>

<script>
export default {
  name: "Toasts",
  props: ["error", "warning", "success"],
};
</script>
