import { Container, getContainer } from "@cloudflare/containers";

/**
 * Durable Object class managing the Spring Boot container instance lifecycle.
 */
export class SpringBootContainer extends Container {
  // Default HTTP port exposed by the Spring Boot embedded Tomcat container
  defaultPort = 8080;

  // Idle sleep timeout before shutting down container on inactivity
  sleepAfter = "10m";
}

/**
 * Worker environment bindings definition.
 */
export interface Env {
  SPRING_BOOT_CONTAINER: DurableObjectNamespace<SpringBootContainer>;
}

export default {
  /**
   * Forward incoming HTTP requests to the named SpringBootContainer instance.
   * Preserves request method, path, query parameters, headers, and body.
   */
  async fetch(request: Request, env: Env): Promise<Response> {
    try {
      // Forward request to a dedicated named container instance
      const container = getContainer(env.SPRING_BOOT_CONTAINER, "spring-boot-instance");
      return await container.fetch(request);
    } catch (error) {
      // Log error internally without exposing credentials or sensitive details
      console.error(
        "Container request failed:",
        error instanceof Error ? error.message : "Unknown error"
      );

      return new Response(
        JSON.stringify({
          error: "Bad Gateway",
          message: "The container service is currently unavailable or encountered an error.",
          status: 502,
        }),
        {
          status: 502,
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
    }
  },
};
